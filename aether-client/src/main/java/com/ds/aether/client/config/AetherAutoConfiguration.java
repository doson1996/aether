package com.ds.aether.client.config;

import com.ds.aether.client.executor.SpringExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ds
 * @date 2025/4/10
 * @description aether自动配置累
 */
@Configuration
@ComponentScan("com.ds.aether")
public class AetherAutoConfiguration {

    @Bean
    public SpringExecutor springExecutor() {
        return new SpringExecutor();
    }

}
