package com.ds.aether.server.storage;

import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.ds.aether.core.context.SpringContext;
import com.ds.aether.core.model.server.ExecutorInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/7/29
 * @description
 */
@Component
public class ExecutorStorageRoute implements ExecutorStorage {

    @Value("${aether.server.executor.storage.type:mongo}")
    private String executorStorageType;

    private ExecutorStorage getExecutorStorage() {
        String finalExecutorStorageType = "";
        if (StrUtil.isEmpty(executorStorageType)) {
            finalExecutorStorageType = "mongo";
        } else {
            finalExecutorStorageType = executorStorageType;
        }
        return SpringContext.getContext().getBean(finalExecutorStorageType + "ExecutorStorage", ExecutorStorage.class);
    }

    @Override
    public Map<String, ExecutorInfo> findAvailableExecutors() {
        return getExecutorStorage().findAvailableExecutors();
    }

    @Override
    public Map<String, ExecutorInfo> findAll() {
        return getExecutorStorage().findAll();
    }

    @Override
    public ExecutorInfo find(String name) {
        return getExecutorStorage().find(name);
    }

    @Override
    public boolean exist(String name) {
        return getExecutorStorage().exist(name);
    }

    @Override
    public boolean add(ExecutorInfo executorInfo) {
        return getExecutorStorage().add(executorInfo);
    }

    @Override
    public boolean remove(String name) {
        return getExecutorStorage().remove(name);
    }

    @Override
    public void removeAll() {
        getExecutorStorage().removeAll();
    }

    @Override
    public void update(ExecutorInfo executorInfo) {
        getExecutorStorage().update(executorInfo);
    }
}
