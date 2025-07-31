package com.ds.aether.server.scheduler;

/**
 * @author ds
 * @date 2025/7/31
 * @description 调度器上下文
 */
public class SchedulerContext {

    private static final CronScheduler SCHEDULER;

    static {
        // 启动调度器
        SCHEDULER = new CronScheduler();
        SCHEDULER.start();
    }

    /**
     * 调度任务
     *
     * @param cronExpression
     * @param jobName
     */
    public static void schedule(String cronExpression, String jobName) {
        SCHEDULER.schedule(cronExpression, jobName);
    }

    /**
     * 取消任务
     *
     * @param jobName
     */
    public static void cancel(String jobName) {
        SCHEDULER.cancel(jobName);
    }

}
