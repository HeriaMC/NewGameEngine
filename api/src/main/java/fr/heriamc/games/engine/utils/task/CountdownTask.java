package fr.heriamc.games.engine.utils.task;

import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public abstract class CountdownTask implements Task<CountdownTask> {

    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> future;

    protected final int duration;

    protected final AtomicInteger secondsLeft;
    protected final AtomicBoolean started, cancelled, finished;

    public CountdownTask(int duration, boolean virtual) {
        this.executor = virtual ? VirtualThreading.scheduledPool : MultiThreading.scheduledPool;
        this.duration = duration;
        this.secondsLeft = new AtomicInteger(duration);
        this.started = new AtomicBoolean(false);
        this.cancelled = new AtomicBoolean(false);
        this.finished = new AtomicBoolean(true);
    }

    public CountdownTask(int duration) {
        this(duration, true);
    }

    @Override
    public void run() {
        if (future != null && !future.isDone()
                && (started.get() || !finished.get() || cancelled.get())) return;

        started.set(true);
        cancelled.set(false);
        finished.set(false);
        secondsLeft.set(duration);
        onStart();

        //log.info("[OnStart] Started: {} | Finished: {} | Cancelled: {} | SecondsLeft: {}", started.get(), finished.get(), cancelled.get(), secondsLeft.get());
        future = executor.scheduleAtFixedRate(() -> {
            if (secondsLeft.get() <= 0 || cancelled.get()) {

                if (!cancelled.get()) onComplete();
                else onCancel();

                finished.set(true);
                started.set(false);
                //log.info("[OnComplete] Started: {} | Finished: {} | Cancelled: {} | SecondsLeft: {}", started.get(), finished.get(), cancelled.get(), secondsLeft.get());
                future.cancel(false);
                return;
            }

            //log.info("[OnNext] Started: {} | Finished: {} | Cancelled: {} | SecondsLeft: {}", started.get(), finished.get(), cancelled.get(), secondsLeft.get());
            onNext(this);
            secondsLeft.decrementAndGet();
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void reset() {
        setSecondsLeft(duration);
        started.set(false);
    }

    @Override
    public void cancel() {
        if (future != null)
            future.cancel(false);

        started.set(false);
        cancelled.set(true);
        finished.set(true);
        onCancel();
    }

    @Override
    public void setSecondsLeft(int seconds) {
        this.secondsLeft.set(seconds);
    }

}