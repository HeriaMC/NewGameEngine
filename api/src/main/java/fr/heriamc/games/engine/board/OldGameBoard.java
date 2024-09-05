//package fr.heriamc.games.engine.board;
//
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import fr.heriamc.games.engine.SimpleGame;
//import fr.heriamc.games.engine.player.BaseGamePlayer;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.bukkit.entity.Player;
//
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.function.Supplier;
//
//@Getter
//@Setter
//@Slf4j
//public abstract class OldGameBoard<M extends SimpleGame<G, ?>, G extends BaseGamePlayer> {
//
//    protected final M game;
//
//    protected Supplier<String> title;
//
//    private final ConcurrentMap<UUID, FastBoard> boards;
//    private final ScheduledExecutorService monoExecutor, threadPool;
//
//    public OldGameBoard(M game, Supplier<String> title) {
//        this.game = game;
//        this.title = title;
//        this.boards = new ConcurrentHashMap<>(game.getGameSize().calculateMapCapacity());
//        this.monoExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("board-mono-thread-%d").build());
//        this.threadPool = Executors.newScheduledThreadPool(5, new ThreadFactoryBuilder().setNameFormat("board-thread-%d").build());
//        this.updateBoards();
//    }
//
//    public abstract List<String> getLines(G gamePlayer);
//
//    public void updateTitle(Supplier<String> title) {
//        this.title = title;
//        boards.values().forEach(fastBoard -> fastBoard.updateTitle(title.get()));
//    }
//
//    public void updateLines(UUID uuid) {
//        final var board = boards.get(uuid);
//        final var gamePlayer = game.getNullablePlayer(uuid);
//
//        if (board == null || gamePlayer == null) return;
//
//        board.updateTitle(title.get());
//        board.updateLines(getLines(gamePlayer));
//    }
//
//    private void updateBoards() {
//        threadPool.scheduleAtFixedRate(() -> boards.keySet().forEach(this::updateBoard), 1, 1, TimeUnit.SECONDS);
//    }
//
//    public void updateBoard(UUID uuid) {
//        monoExecutor.execute(() -> updateLines(uuid));
//    }
//
//    @SuppressWarnings("unchecked")
//    public void addViewer(BaseGamePlayer gamePlayer) {
//        boards.computeIfAbsent(gamePlayer.getUuid(), uuid -> {
//            final var board = new FastBoard(gamePlayer.getPlayer());
//
//            board.updateTitle(title.get());
//            board.updateLines(getLines((G) gamePlayer));
//            return board;
//        });
//    }
//
//    public void removeViewer(UUID uuid, FastBoard board) {
//        boards.remove(uuid);
//        board.delete();
//    }
//
//    public void removeViewer(UUID uuid) {
//        boards.remove(uuid).delete();
//    }
//
//    public void removeViewer(Player player) {
//        removeViewer(player.getUniqueId());
//    }
//
//    public void shutdown() {
//        boards.clear();
//        monoExecutor.shutdownNow();
//        threadPool.shutdownNow();
//    }
//
//}