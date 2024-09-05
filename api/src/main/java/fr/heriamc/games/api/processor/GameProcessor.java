package fr.heriamc.games.api.processor;

import fr.heriamc.games.engine.MiniGame;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;

public interface GameProcessor<M extends MiniGame> {

    ScheduledExecutorService getExecutorService();
    BlockingQueue<CompletableFuture<M>> getQueue();

    void process();

    void addGame(M game);

    void shutdown();

}
