package concurrency.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConcurrencyManager {
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduler;

    private ConcurrencyManager() {
        executorService = Executors.newFixedThreadPool(30);
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private static class Holder {
        private static final ConcurrencyManager INSTANCE = new ConcurrencyManager();
    }

    public static ConcurrencyManager getInstance() {
        return Holder.INSTANCE;
    }

    public void submitTask(Runnable task) {
        executorService.submit(task);
    }

    public void submitScheduledTask(Runnable task, long initialDelay, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(task, initialDelay, period, timeUnit);
    }

    public void shutdown() {
        executorService.shutdownNow();
        scheduler.shutdownNow();
    }
}