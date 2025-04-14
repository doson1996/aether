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

        switch (type) {
            // 选择随机选择器
            case "random":
                executorSelector = new RandomExecutorSelector(executorStorage);
                break;
            // 轮询选择器
            case "poll":
                executorSelector = new PollExecutorSelector(executorStorage);
                break;
            // 没有找到对应的选择器，使用随机选择器的情况
            default:
                // 方便后面put时当key使用 （没有找到对应的选择器，使用随机选择器的情况）
                type = "random";
                executorSelector = new RandomExecutorSelector(executorStorage);
                break;
        }

        SELECTORS.putIfAbsent(type, executorSelector);
        return executorSelector;
    }

}