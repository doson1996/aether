package com.ds.aether.server.executor;

import com.ds.aether.server.storage.ExecutorStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ds
 * @date 2025/4/14
 * @description todo 执行器选择器工厂
 */
public class ExecutorSelectorFactory {

    /**
     * 执行器选择器
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
        ExecutorSelector executorSelector = SELECTORS.get(type);
        if (executorSelector != null)
            return executorSelector;

        if ("poll".equals(type)) {
            PollExecutorSelector pollExecutorSelector = new PollExecutorSelector(executorStorage);
            SELECTORS.put(type, pollExecutorSelector);
            return pollExecutorSelector;
        }

        return new RandomExecutorSelector(executorStorage);
    }

}
