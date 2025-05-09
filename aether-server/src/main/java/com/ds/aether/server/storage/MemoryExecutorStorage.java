package com.ds.aether.server.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.ds.aether.core.constant.ExecutorStatus;
import com.ds.aether.core.model.server.ExecutorInfo;
import org.springframework.stereotype.Repository;

/**
 * @author ds
 * @date 2025/4/13
 * @description 内存执行器存储
 */
@Repository
public class MemoryExecutorStorage implements ExecutorStorage {

    private static final Map<String, ExecutorInfo> EXECUTORS = new ConcurrentHashMap<>();

    @Override
    public Map<String, ExecutorInfo> findAvailableExecutors() {
        Map<String, ExecutorInfo> result = EXECUTORS.entrySet().stream()
                .filter(entry -> !ExecutorStatus.OFFLINE.equals(entry.getValue().getStatus()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return result;
    }

    @Override
    public Map<String, ExecutorInfo> findAll() {
        return EXECUTORS;
    }

    @Override
    public ExecutorInfo find(String name) {
        return EXECUTORS.get(name);
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

    @Override
    public void update(ExecutorInfo executorInfo) {
        add(executorInfo);
    }

}
