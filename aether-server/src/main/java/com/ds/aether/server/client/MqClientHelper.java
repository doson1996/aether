package com.ds.aether.server.client;

import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.server.ExecutorInfo;

/**
 * @author ds
 * @date 2025/8/21
 * @description 监控
 */
public class MqClientHelper implements ClientHelper {

    @Override
    public boolean sendTaskToExecutor(ExecutorInfo executor, ExecJobParam param) {
        return false;
    }

}
