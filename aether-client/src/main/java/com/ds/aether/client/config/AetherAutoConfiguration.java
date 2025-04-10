package com.ds.aether.client.config;

import com.ds.aether.client.executor.SpringExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Configuration
@ComponentScan("com.ds.aether.client")
public class AetherAutoConfiguration {

    @Bean
    public SpringExecutor springExecutor() {
        return new SpringExecutor();
    }

}
