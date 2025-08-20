package com.ds.aether.server.service;

/**
 * @author ds
 * @date 2025/8/20
 * @description
 */
public interface AccessLogService {

    /**
     * 记录访问日志
     * @param className
     * @param methodName
     * @param module
     * @param operation
     * @param params
     * @param result
     * @param duration
     * @param success
     * @param errorMsg
     */
    void logAccess(String className, String methodName, String module,
                   String operation, String params, String result,
                   long duration, boolean success, String errorMsg);

}
