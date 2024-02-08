package concurrency.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyManager {
    private final ExecutorService executorService;

    private ConcurrencyManager() {
        executorService = Executors.newFixedThreadPool(30);
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

    public void shutdown() {
        executorService.shutdown();
    }
}