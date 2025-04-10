package com.ds.aether.client.executor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Slf4j
public abstract class AbstractExecutor implements Executor {

//    private static final Map<String, ExecutorInfo> EXECUTORS = new ConcurrentHashMap<>();

    /**
     * 项目启动之后
     */
    public void start() {
        // 初始化所有任务
        initJobs();
        // 注册执行器
        registerExecutor();
    }

}
