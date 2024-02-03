package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyManager {
    private static ConcurrencyManager instance;

    private static ExecutorService executorService;
    private ConcurrencyManager() {
        executorService = Executors.newScheduledThreadPool(20);
    }

    public static ConcurrencyManager getInstance() {
        if (instance == null) {
            instance = new ConcurrencyManager();
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
