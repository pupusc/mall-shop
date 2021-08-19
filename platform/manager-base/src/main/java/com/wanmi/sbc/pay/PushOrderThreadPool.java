package com.wanmi.sbc.pay;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @ClassName PushOrderThreadPool
 * @Description 异步推送订单线程池
 * @Author wugongjiang
 * @Date 2021-03-06
 **/
@Configuration
@EnableAsync
public class PushOrderThreadPool {

    private static final int CORE_POOL_SIZE = 10;
    private static final String THREAD_NAME = "pushOrder";
    private static final int MAXI_NUM_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 30;
    private static final int LINK_BLOCK_QUEUE_SIZE = 1000;


    private LinkedBlockingQueue pushOrderlinkedBlockingQueue;

    @Bean(name = "pushOrder")
    public Executor myExecutor(){
        pushOrderlinkedBlockingQueue = new LinkedBlockingQueue<Runnable>(LINK_BLOCK_QUEUE_SIZE);
        ThreadFactory factory = new CustomizableThreadFactory(THREAD_NAME);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXI_NUM_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                pushOrderlinkedBlockingQueue,
                factory,
                new ThreadPoolExecutor.AbortPolicy());
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

}
