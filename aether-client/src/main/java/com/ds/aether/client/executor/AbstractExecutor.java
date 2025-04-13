package com.ds.aether.client.executor;

import com.ds.aether.client.context.AetherContext;
import com.ds.aether.client.job.JobInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ds
 * @date 2025/4/10
 * @description 执行器抽象类
 */
@Slf4j
public abstract class AbstractExecutor implements Executor {

    /**
     * 注册任务信息
     *
     * @param jobName
     * @param jobInfo
     */
    protected static void registerJobInfo(String jobName, JobInfo jobInfo) {
        AetherContext.addJobInfo(jobName, jobInfo);
    }

    /**
     * 判断任务信息是否已存在
     *
     * @param jobName
     */
    protected static boolean existJobInfo(String jobName) {
        return AetherContext.containsJobInfo(jobName);
    }

    /**
     * 项目启动之后
     */
    public void start() {
        try {
            // 初始化所有任务
            initJobs();
            // 注册执行器
            registerExecutor();
            // 启动心跳任务
            startHeartbeatTask();
        } catch (Exception e) {
            log.error("aether客户端初始化异常：", e);
        }
    }

    /**
     * 发生心跳逻辑
     */
    protected abstract void sendHeartbeat();

    /**
     * 心跳间隔，单位秒
     */
    private static final int HEARTBEAT_INTERVAL = 5;

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    /**
     * 启动心跳任务
     */
    private void startHeartbeatTask() {
        executorService.scheduleAtFixedRate(this::sendHeartbeat, 0, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

}
