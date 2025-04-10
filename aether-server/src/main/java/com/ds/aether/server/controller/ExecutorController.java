package com.ds.aether.server.controller;

import cn.hutool.core.util.StrUtil;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.client.RegisterParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.ExecutorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ds
 * @date 2025/4/10
 * @description 执行器相关api
 */
@Slf4j
@RestController
@RequestMapping(ServerConstant.EXECUTOR_PATH)
public class ExecutorController {

    private static final Map<String, ExecutorInfo> EXECUTORS = new ConcurrentHashMap<>();

    @PostMapping("exec-job")
    public Result<String> execJob(ExecJobParam param) {
        return Result.ok("执行job成功!");
    }

    @GetMapping("list")
    public Result<Map<String, ExecutorInfo>> list() {
        return Result.okData(EXECUTORS);
    }

    @PostMapping("register")
    public Result<String> register(@RequestBody RegisterParam param) {
        String name = param.getName();
        String host = param.getHost();

        if (StrUtil.hasBlank(name, host)) {
            return Result.fail("参数错误!");
        }

        if (EXECUTORS.containsKey(name)) {
            return Result.fail("执行器已存在!");
        }
        ExecutorInfo executorInfo = new ExecutorInfo(name, host, param.getContextPath());
        EXECUTORS.put(executorInfo.getName(), executorInfo);
        return Result.ok("注册执行器成功!");
    }

    @DeleteMapping("remove")
    public Result<String> remove(String name) {
        if (StrUtil.isBlank(name)) {
            return Result.fail("注册执行器名称不能为空!");
        }
        EXECUTORS.remove(name);
        return Result.ok("移除执行器成功!");
    }

    @DeleteMapping("remove-all")
    public Result<String> removeAll() {
        EXECUTORS.clear();
        return Result.ok("执行器已清空!");
    }

}
