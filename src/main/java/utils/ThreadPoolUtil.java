package utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtil {

    public static ThreadPoolExecutor producerThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 600, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024), new ThreadFactory() {
        private AtomicInteger threadNum = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            String threadName = "producer-thread-" + threadNum.incrementAndGet();
            System.out.println("create thread [" + threadName + "]");
            return new Thread(r, threadName);
        }
    });

    public static ThreadPoolExecutor consumerThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 600, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024), new ThreadFactory() {
        private AtomicInteger threadNum = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            String threadName = "consumer-thread-" + threadNum.incrementAndGet();
            System.out.println("create thread [" + threadName + "]");
            return new Thread(r, threadName);
        }
    });


}
