package fr.heriamc.games.engine.map;

import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.*;

@Getter
@Slf4j
public abstract class GameMapCleaner<L extends GameMapLoader<?>> {

    private final L mapLoader;

    private final File mapDir;

    private final MapManager<?> mapManager;
    private final BlockingQueue<CompletionStage<File>> queue;

    private ScheduledFuture<?> scanFuture, cleanUpFuture;

    public GameMapCleaner(File mapDir, MapManager<?> mapManager, L mapLoader) {
        this.mapDir = mapDir;
        this.mapManager = mapManager;
        this.mapLoader = mapLoader;
        this.queue = new LinkedBlockingQueue<>(10);
        this.schedule();
    }

    private void schedule() {
        this.scanFuture = MultiThreading.schedule(this::scan, 0, 2, TimeUnit.SECONDS);
        this.cleanUpFuture = MultiThreading.schedule(this::cleanUp, 0, 2, TimeUnit.SECONDS);
    }

    public abstract void cleanUp();
    public abstract void scan();

    public void shutdown() {
        if (scanFuture != null && !scanFuture.isCancelled())
            scanFuture.cancel(true);

        if (cleanUpFuture != null && !cleanUpFuture.isCancelled())
            cleanUpFuture.cancel(true);

        queue.forEach(stage -> stage.toCompletableFuture().join());
        queue.clear();
    }

    public void addFile(File file) {
        queue.add(CompletableFuture.supplyAsync(() -> file));
    }

}