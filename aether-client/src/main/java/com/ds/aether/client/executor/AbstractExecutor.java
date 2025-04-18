package com.ds.aether.client.executor;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ds.aether.client.context.AetherContext;
import com.ds.aether.core.job.JobInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/4/10
 * @description 执行器抽象类
 */
@Slf4j
public abstract class AbstractExecutor implements Executor {

    /**
     * 心跳间隔，单位秒
     */
    private static final int HEARTBEAT_INTERVAL = 5;

    /**
     * 心跳线程池
     */
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

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
     * 获取所有任务信息
     */
    protected static Map<String, JobInfo> getAllJobInfo() {
        return AetherContext.allJobInfo();
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
            // 发送任务信息给服务端
            sendJobInfo();
            // 启动心跳任务
            startHeartbeatTask();
            log.debug("aether客户端启动成功");
        } catch (Exception e) {
            log.error("aether客户端初始化异常：", e);
        }
    }

    /**
     * 发送任务信息给服务端
     */
    protected abstract void sendJobInfo();

    /**
     * 发生心跳逻辑
     */
    protected abstract void sendHeartbeat();

    /**
     * 启动心跳任务
     */
    private void startHeartbeatTask() {
        executorService.scheduleAtFixedRate(this::sendHeartbeat, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

}
