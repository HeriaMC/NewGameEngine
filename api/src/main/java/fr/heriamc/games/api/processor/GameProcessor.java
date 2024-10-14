package fr.heriamc.games.api.processor;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.Pair;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;

public interface GameProcessor<M extends MiniGame> {

    ScheduledExecutorService getExecutorService();
    BlockingQueue<CompletableFuture<Pair<UUID, M>>> getQueue();

    void process();

    void addGame(UUID uuid, M game);

    void shutdown();

}