package com.ds.aether.server.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ds
 * @date 2025/8/29
 * @description
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 获取请求的URI
            String requestURI = request.getRequestURI();

            if (StrUtil.isEmpty(requestURI)) {
                return false;
            }

            // 允许访问的路径（无需登录）
            if (requestURI.startsWith("/api/auth") ||
                    "/login".equals(requestURI) ||
                    "/api/page/login".equals(requestURI) ||
                    "/favicon.ico".equals(requestURI) ||
                    requestURI.startsWith("/css/") ||
                    requestURI.startsWith("/js/") ||
                    requestURI.startsWith("/images/")) {
                return true;
            }

            // 检查是否已登录
            if (StpUtil.isLogin()) {
                return true;
            }
        } catch (Exception e) {
            log.error("检查是否已登录异常：", e);
        }
        // 未登录，重定向到登录页
        String redirectUrl = "/api/page/login";
        response.sendRedirect(redirectUrl);
        return false;
    }

}
