package com.ds.aether.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "com.ds.aether")
public class AetherApplication {
    public static void main(String[] args) {
        SpringApplication.run(AetherApplication.class, args);
    }
}
