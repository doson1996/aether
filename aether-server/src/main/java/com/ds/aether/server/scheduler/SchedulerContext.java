package com.ds.aether.server.scheduler;

import com.ds.aether.core.context.SpringContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author ds
 * @date 2025/7/31
 * @description 调度器上下文
 */
public class SchedulerContext implements Scheduler, InitializingBean, DisposableBean {

    @Value("${aether.server.scheduler.type:cronScheduler}")
    private String schedulerType;

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

    @Override
    public boolean isScheduled(String jobName) {
        return getScheduler().isScheduled(jobName);
    }

    @Override
    public Long getScheduledTaskCount() {
        return getScheduler().getScheduledTaskCount();
    }

    private Scheduler getScheduler() {
        return SpringContext.getContext().getBean(schedulerType, Scheduler.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getScheduler().start();
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }
}
