package com.ds.aether.server.storage;

import com.ds.aether.core.model.server.ExecutorInfo;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ds
 * @date 2025/4/13
 * @description 内存执行器存储
 */
@Repository
public class MemoryExecutorStorage implements ExecutorStorage {

    private static final Map<String, ExecutorInfo> EXECUTORS = new ConcurrentHashMap<>();

    @Override
    public Map<String, ExecutorInfo> findAll() {
        return EXECUTORS;
    }

    @Override
    public boolean exist(String name) {
        return EXECUTORS.containsKey(name);
    }

    @Override
    public boolean add(ExecutorInfo executorInfo) {
        // 参数校验
        if (executorInfo == null || executorInfo.getName() == null) {
            return false;
        }

        EXECUTORS.put(executorInfo.getName(), executorInfo);
        return true;
    }

    @Override
    public boolean remove(String name) {
        ExecutorInfo remove = EXECUTORS.remove(name);
        return remove != null;
    }

    @Override
    public void removeAll() {
        EXECUTORS.clear();
    }

}
