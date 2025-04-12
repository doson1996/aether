package com.ds.aether.server.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.constant.ClientConstant;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.client.RegisterParam;
import com.ds.aether.core.model.server.ExecutorInfo;
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

    @Override
    public Result<String> execJob(ExecJobParam param) {
        // 获取所有执行器
        Map<String, ExecutorInfo> executors = executorStorage.findAll();
        if (executors.isEmpty()) {
            return Result.fail("没有可用的执行器!");
        }

        // 轮询选择执行器
        int index = (int) (System.currentTimeMillis() % executors.size());
        String[] executorNames = executors.keySet().toArray(new String[0]);
        ExecutorInfo selectedExecutor = executors.get(executorNames[index]);

        // 将任务发送到指定的执行器
        boolean taskExecuted = sendTaskToExecutor(selectedExecutor, param);
        if (taskExecuted) {
            return Result.ok("任务已成功分配给执行器: " + selectedExecutor.getName());
        } else {
            return Result.fail("任务分配失败!");
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
        log.info("发送任务【{}】到执行器【{}】", param.getJobName(), executor.getName());
        try {
            String resultStr = HttpUtil.post(executor.getHost() + executor.getContextPath() + ClientConstant.CLIENT_EXEC_JOB_PATH, JSONObject.toJSONString(param));
            if (StrUtil.isNotBlank(resultStr)) {
                JSONObject resultJson = JSONObject.parseObject(resultStr);
                if (resultJson.getBoolean("success")) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("发送任务【{}】到执行器【{}】异常：", param.getJobName(), executor.getName(), e);
        }
        return false;
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
            return Result.fail("参数错误!");
        }

        if (executorStorage.exist(name)) {
            return Result.fail("执行器已存在!");
        }
        ExecutorInfo executorInfo = new ExecutorInfo(name, host, param.getContextPath());
        executorStorage.add(executorInfo);
        return Result.ok("注册执行器成功!");
    }

    @Override
    public Result<String> remove(String name) {
        if (StrUtil.isBlank(name)) {
            return Result.fail("注册执行器名称不能为空!");
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
    public Result<String> heartbeat(String name) {
        return null;
    }

}
