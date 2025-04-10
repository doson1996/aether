package com.ds.aether.client.config;

import com.ds.aether.client.executor.SpringExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Configuration
public class AetherAutoConfiguration {

    @Bean
    public SpringExecutor springExecutor() {
        return new SpringExecutor();
    }

}
