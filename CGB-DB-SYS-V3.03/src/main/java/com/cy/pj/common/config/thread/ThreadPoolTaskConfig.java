package com.cy.pj.common.config.thread;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @FileName LiuTaskExecutorConfig2
 * @Description
 * @Author susu
 * @date 2022-03-09
 **/
@Configuration
public class ThreadPoolTaskConfig{

    private static int corePoolSize = 2;//核心线程
    private static int maxPoolSize = 3;//最大线程
    private static int queueCapacity = 1;//队列最大长度
    private static int keepAliveSeconds = 60;//线程池维护线程所允许的空闲时间
    private static String threadNamePrefix = "susu2-thread-";

    private static volatile ThreadPoolTaskExecutor poolTaskExecutor;
    
    @Bean
    public static Executor getSuSuAsyncTaskExecutor2(){
        if(poolTaskExecutor == null){
            synchronized (ThreadPoolTaskExecutor.class){
                if(poolTaskExecutor == null){
                    poolTaskExecutor = new ThreadPoolTaskExecutor();
                    poolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
                    poolTaskExecutor.setCorePoolSize(corePoolSize);
                    poolTaskExecutor.setMaxPoolSize(maxPoolSize);
                    poolTaskExecutor.setQueueCapacity(queueCapacity);
//                    poolTaskExecutor.setThreadFactory(Executors.defaultThreadFactory());
                    poolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
                    // 拒绝策略
                    // 默认的策略 AbortPolicy() 超过队列数量，抛出TaskRejectedException
                    // CallerRunsPolicy() 若一达到待处理的队列长度，接下来的任务将由主线程处理
                    poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//线程池对拒绝任务（无线程可用的）的处理策略
                    poolTaskExecutor.initialize();//初始化
                }
            }
        }
        return poolTaskExecutor;
    }

}


