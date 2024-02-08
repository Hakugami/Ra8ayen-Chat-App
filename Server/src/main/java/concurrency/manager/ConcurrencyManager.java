package concurrency.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyManager {
    private static ConcurrencyManager instance;
    private final ExecutorService executorService;

    private ConcurrencyManager() {
        executorService = Executors.newFixedThreadPool(30);
    }

    public static synchronized ConcurrencyManager getInstance() {
        if (instance == null) {
            instance = new ConcurrencyManager();
        }
        return instance;
    }

    public void submitTask(Runnable task) {
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}