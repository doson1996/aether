package com.ds.aether.server.executor;

import com.ds.aether.server.storage.ExecutorStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ds
 * @date 2025/4/14
 * @description 执行器选择器工厂
 */
public class ExecutorSelectorFactory {

    /**
     * 执行器选择器缓存
     */
    private static final Map<String, ExecutorSelector> SELECTORS = new ConcurrentHashMap<>();

    /**
     * 创建执行器选择器
     *
     * @param type            执行器选择器类型
     * @param executorStorage 执行器存储
     * @return 执行器选择器
     */
    public static ExecutorSelector create(String type, ExecutorStorage executorStorage) {
        // 参数校验
        if (type == null || executorStorage == null) {
            return null;
        }
        
        return SELECTORS.computeIfAbsent(type, key -> {
            switch (key) {
                case "random":
                    return new RandomExecutorSelector(executorStorage);
                case "poll":
                    return new PollExecutorSelector(executorStorage);
                default:
                    // 默认返回随机选择器
                    return new RandomExecutorSelector(executorStorage);
            }
        });
    }

}