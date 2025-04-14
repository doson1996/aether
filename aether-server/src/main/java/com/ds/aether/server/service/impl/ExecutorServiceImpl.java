package com.ds.aether.server.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ds.aether.core.constant.ExecutorStatus;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.HeartbeatParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.ResultCode;
import com.ds.aether.core.model.client.RegisterParam;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.client.ClientHelper;
import com.ds.aether.server.executor.ExecutorSelector;
import com.ds.aether.server.executor.ExecutorSelectorFactory;
import com.ds.aether.server.service.ExecutorService;
import com.ds.aether.server.storage.ExecutorStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ds
 * @date 2025/4/13
 * @description
 */
@Slf4j
@Service
public class ExecutorServiceImpl implements ExecutorService {

    @Qualifier("memoryExecutorStorage")
    @Resource
    private ExecutorStorage executorStorage;

    @Resource
    private ClientHelper clientHelper;

    @Override
    public Result<String> execJob(ExecJobParam param) {
        if (param == null || StrUtil.isBlank(param.getJobName())) {
            return Result.fail("参数错误!");
        }

        // 选择一个执行器
//        ExecutorSelector executorSelector = new RandomExecutorSelector(executorStorage);
        ExecutorSelector executorSelector = ExecutorSelectorFactory.create("poll", executorStorage);
        ExecutorInfo selectedExecutor = executorSelector.selectedExecutor(param);
        if (selectedExecutor == null) {
            return Result.fail("没有可用的执行器!");
        }

        // 将任务发送到指定的执行器
        boolean taskExecuted = sendTaskToExecutor(selectedExecutor, param);
        if (taskExecuted) {
            return Result.ok("任务[" + param.getJobName() + "]已成功分配给执行器: " + selectedExecutor.getName());
        } else {
            return Result.fail("任务[" + param.getJobName() + "]分配失败!");
        }
    }

    /**
     * 将任务发送到指定的执行器
     *
     * @param executor
     * @param param
     * @return
     */
    private boolean sendTaskToExecutor(ExecutorInfo executor, ExecJobParam param) {
        return clientHelper.sendTaskToExecutor(executor, param);
    }

    @Override
    public Result<Map<String, ExecutorInfo>> list() {
        return Result.okData(executorStorage.findAll());
    }

    @Override
    public Result<String> register(RegisterParam param) {
        String name = param.getName();
        String host = param.getHost();

        if (StrUtil.hasBlank(name, host)) {
            return Result.failWithParameterError("参数错误!");
        }

        if (executorStorage.exist(name)) {
            return Result.fail("执行器已存在!");
        }
        ExecutorInfo executorInfo = new ExecutorInfo(name, host, param.getContextPath(), ExecutorStatus.ONLINE);
        executorStorage.add(executorInfo);
        return Result.ok("注册执行器成功!");
    }

    @Override
    public Result<String> remove(String name) {
        if (StrUtil.isBlank(name)) {
            return Result.failWithParameterError("注册执行器名称不能为空!");
        }
        executorStorage.remove(name);
        return Result.ok("移除执行器成功!");
    }

    @Override
    public Result<String> removeAll() {
        executorStorage.removeAll();
        return Result.ok("执行器已清空!");
    }

    @Override
    public Result<String> heartbeat(HeartbeatParam param) {
        if (param == null || StrUtil.isBlank(param.getName())) {
            return Result.failWithParameterError("执行器名称不能为空!");
        }

        ExecutorInfo executorInfo = executorStorage.find(param.getName());
        if (executorInfo == null) {
            return Result.fail(ResultCode.EXECUTOR_NOT_EXIST, "执行器不存在!!");
        }

        // 更新最后心跳时间
        executorInfo.setLastHeartbeat(System.currentTimeMillis());
        // 如果状态为离线，则更新为在线
        if (ExecutorStatus.OFFLINE.equals(executorInfo.getStatus())) {
            executorInfo.setStatus(ExecutorStatus.ONLINE);
        }
        executorStorage.update(executorInfo);
        return Result.ok("心跳接收成功!");
    }

}