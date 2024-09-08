package fr.heriamc.games.api.processor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.api.pool.GameManager;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Getter
@Slf4j
public class GameLoaderProcessor<M extends MiniGame> implements GameProcessor<M> {

    private final GameManager<M> gameManager;

    private final ScheduledExecutorService executorService;
    private final BlockingQueue<CompletableFuture<M>> queue;

    public GameLoaderProcessor(GameManager<M> gameManager) {
        this.gameManager = gameManager;
        this.executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("game-queue-processor-%d").build());
        this.queue = new LinkedBlockingQueue<>();
        this.executorService.scheduleAtFixedRate(this::process, 20, 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public void process() {
        CompletableFuture<M> future = queue.peek();
        
        if (future != null && future.isDone()) {
            try {
                if (queue.isEmpty()) return;

                M game = queue.peek().get();

                if (game.getState() == GameState.LOADING) {
                    log.info("[GameProcessor] START LOADING {}", game.getFullName());
                    game.load();
                    game.setState(GameState.LOADING_IN_PROGRESS);
                }

                if (game.getState() == GameState.WAIT || game.getState() == GameState.ALWAYS_PLAYING) {
                    log.info("[GameProcessor] GAME : {} completely loaded", game.getFullName());
                    //gameManager.forceAddGame(game);
                    queue.poll();
                    log.info("[GameProcessor] QUEUE: ELEMENTS LEFT={}", queue.size());
                }

            } catch (InterruptedException | ExecutionException exception) {
                log.error("GAME LOADER PROCESSOR EXCEPTION", exception);
            }
        }
    }

    @Override
    public void addGame(M game) {
        queue.add(CompletableFuture.supplyAsync(() -> game));
    }

    @Override
    public void shutdown() {
        queue.clear();
        Utils.awaitTerminationAfterShutdown(executorService, 1, TimeUnit.SECONDS);
    }

}