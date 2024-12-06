/**
 * 线程管理器，用于提供统一的线程池服务。
 * @author 汪炜力
 * @version 1.0.0
 */

package com.ssvep.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private static final int THREAD_POOL_SIZE = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private ThreadPoolManager() {
        // 私有构造方法，防止实例化
    }

    /**
     * 提供全局线程池。
     *
     * @return ExecutorService
     */
    public static ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * 关闭线程池
     */
    public static void shutdown() {
        executorService.shutdown();
    }
}
