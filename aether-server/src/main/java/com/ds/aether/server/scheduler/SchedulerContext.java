package com.ds.aether.server.scheduler;

import com.ds.aether.core.context.SpringContext;

/**
 * @author ds
 * @date 2025/7/31
 * @description 调度器上下文
 */
public class SchedulerContext implements Scheduler {

    @Override
    public void start() {
        getScheduler().start();
    }

    @Override
    public void stop() {
        getScheduler().stop();
    }

    /**
     * 调度任务
     *
     * @param cronExpression
     * @param jobName
     */
    @Override
    public void schedule(String cronExpression, String jobName) {
        getScheduler().schedule(cronExpression, jobName);
    }

    /**
     * 取消任务
     *
     * @param jobName
     * @return
     */
    @Override
    public boolean cancel(String jobName) {
        return getScheduler().cancel(jobName);
    }

    private Scheduler getScheduler() {
        return SpringContext.getContext().getBean("cronScheduler", Scheduler.class);
    }

}
