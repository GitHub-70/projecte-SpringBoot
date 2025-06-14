package com.cy.pj.common.config.thread;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @FileName LiuTaskExecutorConfig2
 * @Description
 * @Author susu
 * @date 2022-03-09
 *
 * 线程池长期运行的合理场景
 *      适用场景：
 *      高并发服务（如 Web 服务器、RPC 服务）： 线程池需长期运行以复用线程资源，避免频繁创建/销毁线程的开销。
 *      异步任务处理（如日志记录、消息队列消费）： 线程池持续监听任务队列，确保任务即时处理。
 *      不适用场景：
 *      短时任务（如批处理脚本）： 任务完成后应立即关闭线程池，避免资源浪费。
 *      动态扩展需求（如云原生服务）： 需结合自动扩缩容机制，避免固定线程池占用过多资源。
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
    public Executor getSuSuAsyncTaskExecutor2(){
        if(poolTaskExecutor == null){
            synchronized (ThreadPoolTaskExecutor.class){
                if(poolTaskExecutor == null){
                    poolTaskExecutor = new ThreadPoolTaskExecutor();
                    poolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
                    // 核心线程大小，根据 平均负载 设置合理值。
                    poolTaskExecutor.setCorePoolSize(corePoolSize);
                    poolTaskExecutor.setMaxPoolSize(maxPoolSize);
                    poolTaskExecutor.setQueueCapacity(queueCapacity);
//                    poolTaskExecutor.setThreadFactory(Executors.defaultThreadFactory());
//                    poolTaskExecutor.setAllowCoreThreadTimeOut(true);//允许核心线程超时
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

    /**
     * 关闭线程池
     * 应用正常关闭时（如 Tomcat 停止、Spring Boot 关闭），Spring 会自动触发 @PreDestroy 方法
     */
    @PreDestroy
    public void destroy() {
        if (poolTaskExecutor != null) {
            poolTaskExecutor.shutdown();
        }
    }

}


