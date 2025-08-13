package com.ds.aether.server.config;

import com.ds.aether.server.scheduler.CronScheduler;
import com.ds.aether.server.scheduler.DistributedCronScheduler;
import com.ds.aether.server.scheduler.SchedulerContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author ds
 * @date 2025/8/13
 * @description
 */
@Configuration
public class SchedulerConfig {

    @Bean
    public SchedulerContext schedulerContext() {
        return new SchedulerContext();
    }

    @Bean
    public DistributedCronScheduler distributedCronScheduler(
            RedisTemplate<String, String> redisTemplate) {
        return new DistributedCronScheduler(redisTemplate);
    }

    @Bean
    public CronScheduler cronScheduler() {
        return new CronScheduler();
    }

}
