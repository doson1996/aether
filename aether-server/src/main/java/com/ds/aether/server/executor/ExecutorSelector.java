package com.ds.aether.server.executor;

import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.server.ExecutorInfo;

/**
 * @author ds
 * @date 2025/4/14
 * @description 执行器选择器父类接口
 */
public interface ExecutorSelector {

    /**
     * 选择执行器
     *
     * @param param
     * @return
     */
    ExecutorInfo selectedExecutor(ExecJobParam param);

}
