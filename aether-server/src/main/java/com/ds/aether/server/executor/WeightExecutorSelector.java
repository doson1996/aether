package com.ds.aether.server.executor;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.storage.ExecutorStorage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/4/14
 * @description 权重选择执行器
 */
@Slf4j
public class WeightExecutorSelector extends AbstractExecutorSelector implements ExecutorSelector {

    private final Random random = new Random();

    public WeightExecutorSelector(ExecutorStorage executorStorage) {
        super(executorStorage);
    }

    @Override
    public ExecutorInfo selectedExecutor(ExecJobParam param) {
        Map<String, ExecutorInfo> availableExecutors = findAvailableExecutors();
        if (availableExecutors.isEmpty()) {
            log.warn("没有可用的执行器!");
            return null;
        }

        // 可以根据需要选择轮询或权重方式
        return selectByWeight(availableExecutors);
    }

    /**
     * 根据权重选择执行器
     */
    private ExecutorInfo selectByWeight(Map<String, ExecutorInfo> executors) {
        int totalWeight = executors.values().stream()
                .mapToInt(executor -> executor.getWeight() != null ? executor.getWeight() : 1)
                .sum();

        if (totalWeight <= 0) {
            // 如果权重总和无效，则随机选择
            Object[] executorArray = executors.values().toArray();
            return (ExecutorInfo) executorArray[random.nextInt(executorArray.length)];
        }

        int randomPoint = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (ExecutorInfo executor : executors.values()) {
            int weight = executor.getWeight() != null ? executor.getWeight() : 1;
            currentWeight += weight;
            if (randomPoint < currentWeight) {
                return executor;
            }
        }

        // 理论上不会执行到这里
        return executors.values().iterator().next();
    }
}