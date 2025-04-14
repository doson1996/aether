package com.ds.aether.core.job;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Job {

    /**
     * 任务名（全局唯一，执行任务标识）
     *
     * @return
     */
    String name() default "";

}
