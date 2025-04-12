package com.ds.aether.server.storage;

import com.ds.aether.core.model.server.ExecutorInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ds
 * @date 2025/4/13
 * @description MySQL执行器存储
 */
@Repository
public class MySQLExecutorStorage implements ExecutorStorage {

    @Override
    public Map<String, ExecutorInfo> findAll() {
        return new HashMap<>();
    }

    @Override
    public boolean exist(String name) {
        return false;
    }

    @Override
    public boolean add(ExecutorInfo executorInfo) {
        return false;
    }

    @Override
    public boolean remove(String name) {
        return false;
    }

    @Override
    public void removeAll() {

    }

}