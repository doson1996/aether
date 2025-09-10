package com.ds.aether.server.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(Lists.newArrayList("/api/auth/**",
                        "/api/css/**",
                        "/api/js/**",
                        "/api/images/**",
                        "/api/fonts/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**",
                        "/favicon.ico",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/doc.html",
                        "/error",
                        "/actuator/**"));
    }

}
