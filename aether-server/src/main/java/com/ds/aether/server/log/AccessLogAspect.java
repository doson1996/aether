package com.ds.aether.server.log;

import javax.annotation.Resource;

import com.alibaba.fastjson2.JSON;
import com.ds.aether.server.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/8/20
 * @description
 */
@Aspect
@Component
@Slf4j
public class AccessLogAspect {

    @Resource
    private AccessLogService accessLogService;

    @Around("@annotation(accessLog)")
    public Object around(ProceedingJoinPoint point, AccessLog accessLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getName();

        // 获取请求参数
        Object[] args = point.getArgs();
        String params = accessLog.recordParams() ? JSON.toJSONString(args) : "";

        try {
            // 执行目标方法
            Object result = point.proceed();

            // 记录成功访问日志
            long endTime = System.currentTimeMillis();
            accessLogService.logAccess(className, methodName, accessLog.module(),
                    accessLog.operation(), params,
                    accessLog.recordResult() ? JSON.toJSONString(result) : "",
                    endTime - startTime, true, "");

            return result;
        } catch (Exception e) {
            // 记录失败访问日志
            long endTime = System.currentTimeMillis();
            accessLogService.logAccess(className, methodName, accessLog.module(),
                    accessLog.operation(), params, "",
                    endTime - startTime, false, e.getMessage());
            throw e;
        }
    }

}
