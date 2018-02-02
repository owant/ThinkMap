package com.owant.thinkmap.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by owant on 17/03/2017.
 */

public class ThreadPoolManager {

    private ThreadPoolExecutor mPoolExecutor;
    private final static int default_thread_size = 4;

    private static class ThreadPoolManagerHolder {
        private static ThreadPoolManager instance = new ThreadPoolManager();
    }

    private ThreadPoolManager() {
        mPoolExecutor = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(getBestThreadSize());
    }

    private int getBestThreadSize() {
        return default_thread_size;
    }

    public static ThreadPoolManager getInstance() {
        return ThreadPoolManagerHolder.instance;
    }


}
