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
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.server.service.ExecutorService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/7/31
 * @description
 */
@Slf4j
public class CronScheduler {
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);
    // 用于存储任务的Future，以便可以取消任务
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public CronScheduler() {
        this.scheduler = Executors.newScheduledThreadPool(10);
    }

    public void schedule(String cronExpression, Runnable task, String jobName) {
        if (!running.get()) {
            throw new IllegalStateException("调度器未启动");
        }

        // 如果同名任务已存在，先取消它
        cancel(jobName);

        CronExpression cron = new CronExpression(cronExpression);
        ScheduledFuture<?> future = scheduleNext(cron, task, jobName);
        scheduledTasks.put(jobName, future);
    }

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

    public boolean cancel(String jobName) {
        ScheduledFuture<?> future = scheduledTasks.remove(jobName);
        if (future != null) {
            future.cancel(false); // 不中断正在执行的任务
            return true;
        }
        return false;
    }

    private ScheduledFuture<?> scheduleNext(CronExpression cron, Runnable task, String jobName) {
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
                task.run();
            } finally {
                // 安排下一次执行
                if (running.get() && scheduledTasks.containsKey(jobName)) {
                    ScheduledFuture<?> nextFuture = scheduleNext(cron, task, jobName);
                    scheduledTasks.put(jobName, nextFuture);
                }
            }
        }, delay, TimeUnit.SECONDS);
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
                ExecJobParam execJobParam = new ExecJobParam();
                execJobParam.setJobName(jobName);
                ExecutorService executorService = SpringContext.getContext().getBean(ExecutorService.class);
                executorService.execJob(execJobParam);
                log.debug("执行任务：" + jobName);
            } finally {
                // 安排下一次执行
                if (running.get() && scheduledTasks.containsKey(jobName)) {
                    ScheduledFuture<?> nextFuture = scheduleNext(cron, jobName);
                    scheduledTasks.put(jobName, nextFuture);
                }
            }
        }, delay, TimeUnit.SECONDS);
    }

    public void start() {
        running.set(true);
    }

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

    // 获取当前调度的任务数量
    public int getTaskCount() {
        return scheduledTasks.size();
    }

    // 检查特定任务是否存在
    public boolean isScheduled(String jobName) {
        return scheduledTasks.containsKey(jobName);
    }

    public static void main(String[] args) {
        CronScheduler scheduler = new CronScheduler();
        scheduler.start();

        // 每分钟执行一次的任务
        scheduler.schedule("0 * * * * *", "每分钟任务执行");

        // 每5秒执行一次的任务
        scheduler.schedule("*/5 * * * * *", "每5秒任务执行");


        // 10秒后取消"每5秒任务执行: "任务
//        scheduler.scheduler.schedule(() -> {
//            System.out.println("尝试取消任务: 每5秒任务执行: ");
//            boolean cancelled = scheduler.cancel("每5秒任务执行");
//            System.out.println("任务取消结果: " + cancelled);
//        }, 10, TimeUnit.SECONDS);

        // 运行1分钟后停止
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            scheduler.stop();
        }
    }

}
