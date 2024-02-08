package concurrency.manager;

import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyManager {
    private static ConcurrencyManager instance;
    private final ExecutorService executorService;

    private ConcurrencyManager() {
        executorService = Executors.newFixedThreadPool(20);
    }

    public static synchronized ConcurrencyManager getInstance() {
        if (instance == null) {
            instance = new ConcurrencyManager();
        }
        return instance;
    }

    public void submitRunnable(Runnable task) {
        executorService.submit(task);
    }
    public void submitTask(Task task) {
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void forceShutdown() {
        executorService.shutdownNow();
    }

}