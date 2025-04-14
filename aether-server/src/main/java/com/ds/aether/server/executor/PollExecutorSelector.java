package com.ds.aether.server.executor;

import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.storage.ExecutorStorage;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ds
 * @date 2025/4/14
 * @description 轮询选择执行器
 */
@Slf4j
public class PollExecutorSelector extends AbstractExecutorSelector implements ExecutorSelector {

    private final AtomicInteger currentExecutorIndex = new AtomicInteger(0);

    public PollExecutorSelector(ExecutorStorage executorStorage) {
        super(executorStorage);
    }

    @Override
    public ExecutorInfo selectedExecutor(ExecJobParam param) {
        Map<String, ExecutorInfo> availableExecutors = findAvailableExecutors();
        if (availableExecutors.isEmpty()) {
            log.warn("没有可用的执行器!");
            return null;
        }

        int index = currentExecutorIndex.getAndIncrement() % availableExecutors.size();
        return (ExecutorInfo) availableExecutors.values().toArray()[index];
    }

}
