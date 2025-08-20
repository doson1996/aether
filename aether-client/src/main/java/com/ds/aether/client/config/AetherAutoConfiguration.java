package com.ds.aether.client.config;

import com.ds.aether.client.executor.SpringExecutor;
import com.ds.aether.core.client.ServerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ds
 * @date 2025/4/10
 * @description aether自动配置类
 */
@Configuration
@ComponentScan("com.ds.aether")
public class AetherAutoConfiguration {

    @Value("${aether.server.host:localhost:8081}")
    private String serverHost;

    @Bean
    public SpringExecutor springExecutor() {
        return new SpringExecutor();
    }

    @Bean
    public ServerClient serverClient() {
        return new ServerClient(serverHost);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 队列容量
        executor.setQueueCapacity(100);
        // 线程名称前缀
        executor.setThreadNamePrefix("taskExecutor-");
        executor.initialize();
        return executor;
    }

}
