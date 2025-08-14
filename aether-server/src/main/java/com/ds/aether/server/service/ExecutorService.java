package com.ds.aether.server.service;

import com.ds.aether.core.common.Page;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.HeartbeatParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.client.RegisterParam;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.model.dto.BasePageParam;

import java.util.Map;

/**
 * @author ds
 * @date 2025/4/13
 * @description
 */
public interface ExecutorService {

    /**
     * 执行任务
     *
     * @param param
     * @return
     */
    Result<String> execJob(ExecJobParam param);

    /**
     * 获取所有执行器信息
     *
     * @return
     */
    Result<Map<String, ExecutorInfo>> list();

    /**
     * 注册执行器
     *
     * @param param
     * @return
     */
    Result<String> register(RegisterParam param);

    /**
     * 移除执行器
     *
     * @param name
     * @return
     */
    Result<String> remove(String name);

    /**
     * 移除所有执行器
     *
     * @return
     */
    Result<String> removeAll();

    /**
     * 心跳
     *
     * @param param
     * @return
     */
    Result<String> heartbeat(HeartbeatParam param);

    Result<Page> page(BasePageParam param);

    /**
     * 禁/启用执行器
     *
     * @param name
     * @return
     */
    Result<String> updateDisabled(String name);

}
