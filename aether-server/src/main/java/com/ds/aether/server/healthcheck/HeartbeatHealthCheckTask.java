package com.ds.aether.server.healthcheck;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.ds.aether.core.constant.ExecutorStatus;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.storage.ExecutorStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    private static final Integer CHECK_INTERVAL = 5;

    /**
     * 置为离线时间，单位秒（十秒钟没接收到心跳，下线执行器）
     */
    private static final Long OFFLINE_TIMEOUT = 10L;

    /**
     * 移除时间，单位秒 (五分钟没接收到心跳，移除执行器)
     */
    private static final Long REMOVE_TIMEOUT = 5 * 60L;

    private static final ScheduledExecutorService THREAD_POOL = Executors.newScheduledThreadPool(1);

    @Qualifier("executorStorageRoute")
    @Resource
    private ExecutorStorage executorStorage;

    private static final Map<String, Integer> NONE_HEARTBEAT = new ConcurrentHashMap<>();

    private static final Integer REMOVE_COUNT = 5;

    @Override
    public synchronized void doHealthCheck() {
        Map<String, ExecutorInfo> allExecutors = executorStorage.findAll();
        allExecutors.forEach((name, executorInfo) -> {
            try {
                Long lastHeartbeat = executorInfo.getLastHeartbeat();

                if (lastHeartbeat == null) {
                    if (NONE_HEARTBEAT.containsKey(name)) {
                        if (NONE_HEARTBEAT.get(name) >= REMOVE_COUNT) {
                            log.warn("新执行器{}超过{}次未发送心跳，将移除该执行器", name, REMOVE_COUNT);
                            executorStorage.remove(name);
                        } else {
                            NONE_HEARTBEAT.put(name, NONE_HEARTBEAT.get(name) + 1);
                        }
                    } else {
                        NONE_HEARTBEAT.put(name, 0);
                    }
                    return;
                }

                long currentTimeMillis = System.currentTimeMillis();

                if (currentTimeMillis - lastHeartbeat > OFFLINE_TIMEOUT * 1000) {
                    log.warn("执行器{}心跳超时，将下线该执行器", name);
                    executorInfo.setStatus(ExecutorStatus.OFFLINE);
                    executorStorage.update(executorInfo);
                }

                if (currentTimeMillis - lastHeartbeat > REMOVE_TIMEOUT * 1000) {
                    log.warn("执行器{}心跳超时，将移除该执行器", name);
                    executorStorage.remove(name);
                }
            } catch (Exception e) {
                log.error("健康检查任务执行异常：", e);
            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        THREAD_POOL.scheduleAtFixedRate(this::doHealthCheck, 0, CHECK_INTERVAL, TimeUnit.SECONDS);
    }

}