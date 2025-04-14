package com.ds.aether.server.executor;

import java.util.Map;

import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.storage.ExecutorStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @author ds
 * @date 2025/4/14
 * @description 随机选择执行器
 */
@Slf4j
public class RandomExecutorSelector extends AbstractExecutorSelector implements ExecutorSelector {

    public RandomExecutorSelector(ExecutorStorage executorStorage) {
        super(executorStorage);
    }

    @Override
    public ExecutorInfo selectedExecutor(ExecJobParam param) {
        // 获取所有执行器
        Map<String, ExecutorInfo> executors = findExecutors();
        if (CollectionUtils.isEmpty(executors)) {
            log.warn("没有可用的执行器!");
            return null;
        }

        int index = (int) (System.currentTimeMillis() % executors.size());
        String[] executorNames = executors.keySet().toArray(new String[0]);
        return executors.get(executorNames[index]);
    }

}
