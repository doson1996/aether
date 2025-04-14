package com.ds.aether.server.client;

import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.server.ExecutorInfo;

/**
 * @author ds
 * @date 2025/4/14
 * @description
 */
public interface ClientHelper {

    boolean sendTaskToExecutor(ExecutorInfo executor, ExecJobParam param);

}
