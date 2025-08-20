package com.ds.aether.server.config;

import com.ds.aether.server.scheduler.DistributedCronScheduler;
import com.ds.aether.server.scheduler.SchedulerContext;
import com.ds.aether.server.scheduler.StandaloneScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author ds
 * @date 2025/8/13
 * @description
 */
@Configuration
public class SchedulerConfig {

    @Value("${aether.server.scheduler.corePoolSize:2}")
    private Integer corePoolSize;

    @DependsOn("springContext")
    @Bean
    public SchedulerContext schedulerContext() {
        return new SchedulerContext();
    }

    @Bean
    public DistributedCronScheduler distributedCronScheduler(
            RedisTemplate<String, String> redisTemplate) {
        return new DistributedCronScheduler(redisTemplate, corePoolSize);
    }

    @Bean
    public StandaloneScheduler standaloneScheduler() {
        return new StandaloneScheduler(corePoolSize);
    }

}
