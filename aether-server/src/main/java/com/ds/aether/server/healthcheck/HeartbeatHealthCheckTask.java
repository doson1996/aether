package com.ds.aether.server.healthcheck;

import com.ds.aether.core.constant.ExecutorStatus;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.storage.ExecutorStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ds
 * @date 2025/4/16
 * @description
 */
@Slf4j
@Component
public class HeartbeatHealthCheckTask implements HealthCheckTask, CommandLineRunner {
    /**
     * 心跳间隔，单位秒
     */
    private static final int CHECK_INTERVAL = 1;

    /**
     * 置为离线时间，单位秒（十秒钟没接收到心跳，下线执行器）
     */
    private static final Long OFFLINE_TIMEOUT = 10L;

    /**
     * 移除时间，单位秒 (五分钟没接收到心跳，移除执行器)
     */
    private static final Long REMOVE_TIMEOUT = 5 * 60L;

    private static final ScheduledExecutorService THREAD_POOL = Executors.newScheduledThreadPool(1);

    @Qualifier("memoryExecutorStorage")
    @Resource
    private ExecutorStorage executorStorage;

    @Override
    public synchronized void doHealthCheck() {
        Map<String, ExecutorInfo> allExecutors = executorStorage.findAll();
        allExecutors.forEach((name, executorInfo) -> {
            Long lastHeartbeat = executorInfo.getLastHeartbeat();
            long currentTimeMillis = System.currentTimeMillis();

            if (!ExecutorStatus.ONLINE.equals(executorInfo.getStatus())) {
                return;
            }

            if (currentTimeMillis - lastHeartbeat > OFFLINE_TIMEOUT * 1000) {
                log.warn("执行器{}心跳超时，将下线该执行器", name);
                executorInfo.setStatus(ExecutorStatus.OFFLINE);
                executorStorage.update(executorInfo);
            }

            if (currentTimeMillis - lastHeartbeat > REMOVE_TIMEOUT * 1000) {
                log.warn("执行器{}心跳超时，将移除该执行器", name);
                executorStorage.remove(name);
            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        THREAD_POOL.scheduleAtFixedRate(this::doHealthCheck, 0, CHECK_INTERVAL, TimeUnit.SECONDS);
    }

}