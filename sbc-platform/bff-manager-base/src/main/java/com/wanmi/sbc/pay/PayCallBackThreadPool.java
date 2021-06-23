package com.wanmi.sbc.pay;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @ClassName PayCallBackThreadPool
 * @Description 支付回调线处理程池
 * @Author lvzhenwei
 * @Date 2020/6/30 14:12
 **/
@Configuration
@EnableAsync
public class PayCallBackThreadPool {

    private static final int CORE_POOL_SIZE = 10;
    private static final String THREAD_NAME = "paycallback";
    private static final int MAXI_NUM_SIZE = 20;
    private static final int KEEP_ALIVE_TIME = 30;
    private static final int LINK_BLOCK_QUEUE_SIZE = 1000;


    private LinkedBlockingQueue payCallBacklinkedBlockingQueue;

    @Bean(name = "payCallBack")
    public Executor myExecutor(){
        payCallBacklinkedBlockingQueue = new LinkedBlockingQueue<Runnable>(LINK_BLOCK_QUEUE_SIZE);
        ThreadFactory factory = new CustomizableThreadFactory(THREAD_NAME);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXI_NUM_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                payCallBacklinkedBlockingQueue,
                factory,
                new ThreadPoolExecutor.AbortPolicy());
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

}
