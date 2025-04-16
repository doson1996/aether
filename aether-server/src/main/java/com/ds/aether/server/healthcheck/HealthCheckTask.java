package com.ds.aether.server.healthcheck;

/**
 * @author ds
 * @date 2025/4/16
 * @description 执行器健康检测任务
 */
public interface HealthCheckTask {

    /**
     * 执行健康检查
     */
    void doHealthCheck();

}
