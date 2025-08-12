package com.ds.aether.client.executor;

import com.ds.aether.core.model.Result;

/**
 * @author ds
 * @date 2025/4/10
 * @description 执行器顶级接口
 */
public interface Executor {

    /**
     * 注册执行器
     */
    void registerExecutor();

    /**
     * 初始化任务集合
     */
    void initJobs();

    /**
     * 执行任务
     */
    Result<String> executeJob(String jobName, String params);

}
