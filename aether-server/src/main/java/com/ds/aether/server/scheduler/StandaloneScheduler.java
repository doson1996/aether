package com.ds.aether.server.scheduler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ds.aether.core.context.SpringContext;
import com.ds.aether.server.job.ExecJobHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/7/31
 * @description 单机调度
 */
@Slf4j
public class StandaloneScheduler implements Scheduler {

    private final ScheduledExecutorService scheduler;

    private final AtomicBoolean running = new AtomicBoolean(false);

    // 用于存储任务的Future，以便可以取消任务
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public StandaloneScheduler(Integer corePoolSize) {
        this.scheduler = Executors.newScheduledThreadPool(corePoolSize);
    }

    @Override
    public void schedule(String cronExpression, String jobName) {
        if (!running.get()) {
            throw new IllegalStateException("调度器未启动");
        }

        // 如果同名任务已存在，先取消它
        cancel(jobName);

        CronExpression cron = new CronExpression(cronExpression);
        ScheduledFuture<?> future = scheduleNext(cron, jobName);
        scheduledTasks.put(jobName, future);
    }

    @Override
    public boolean cancel(String jobName) {
        ScheduledFuture<?> future = scheduledTasks.remove(jobName);
        if (future != null) {
            // 不中断正在执行的任务
            future.cancel(false);
            return true;
        }
        return false;
    }

    private ScheduledFuture<?> scheduleNext(CronExpression cron, String jobName) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = now.plusSeconds(1);

        // 查找下一个匹配的时间点
        while (!cron.matches(next)) {
            next = next.plusSeconds(1);

            // 防止无限循环
            if (next.isAfter(now.plusYears(1))) {
                throw new RuntimeException("无法找到匹配的执行时间");
            }
        }

        long delay = java.time.Duration.between(now, next).getSeconds();
        return scheduler.schedule(() -> {
            try {
                // 执行任务
                ExecJobHelper execJobHelper = SpringContext.getContext().getBean(ExecJobHelper.class);
                execJobHelper.start(jobName);
            } finally {
                // 安排下一次执行
                if (running.get() && scheduledTasks.containsKey(jobName)) {
                    ScheduledFuture<?> nextFuture = scheduleNext(cron, jobName);
                    scheduledTasks.put(jobName, nextFuture);
                }
            }
        }, delay, TimeUnit.SECONDS);
    }

    @Override
    public void start() {
        running.set(true);
    }

    @Override
    public void stop() {
        running.set(false);
        // 取消所有已调度的任务
        for (Map.Entry<String, ScheduledFuture<?>> entry : scheduledTasks.entrySet()) {
            entry.getValue().cancel(false);
        }
        scheduledTasks.clear();

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 获取任务数
     *
     * @return
     */
    public int getTaskCount() {
        return scheduledTasks.size();
    }

    /**
     * 判断任务是否已调度
     *
     * @param jobName
     * @return
     */
    @Override
    public boolean isScheduled(String jobName) {
        return scheduledTasks.containsKey(jobName);
    }

    /**
     * 获取已调度的任务数
     *
     * @return
     */
    @Override
    public Long getScheduledTaskCount() {
        return (long) scheduledTasks.size();
    }

}
