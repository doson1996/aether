package com.ds.aether.server.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ds
 * @date 2025/8/20
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLog {

    String module() default "";

    String operation() default "";

    boolean recordParams() default false;

    boolean recordResult() default false;

}