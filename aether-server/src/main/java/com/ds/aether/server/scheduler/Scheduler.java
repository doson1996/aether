package com.ds.aether.server.scheduler;

/**
 * @author ds
 * @date 2025/8/13
 * @description
 */
public interface Scheduler {

    /**
     * 开始调度
     */
    void start();

    /**
     * 停止调度
     */
    void stop();

    /**
     * 调度任务
     *
     * @param cronExpression
     * @param jobName
     */
    void schedule(String cronExpression, String jobName);

    /**
     * 取消任务调度
     */
    boolean cancel(String jobName);

}
