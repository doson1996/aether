package com.ds.aether.server.executor;

import java.util.Map;

import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.storage.ExecutorStorage;

/**
 * @author ds
 * @date 2025/4/14
 * @description 执行器选择器抽象类
 */
public abstract class AbstractExecutorSelector implements ExecutorSelector {

    protected ExecutorStorage executorStorage;

    public AbstractExecutorSelector(ExecutorStorage executorStorage) {
        this.executorStorage = executorStorage;
    }

    /**
     * 查找所有可用执行器
     *
     * @return
     */
    protected Map<String, ExecutorInfo> findAvailableExecutors() {
        return executorStorage.findAvailableExecutors();
    }


}
