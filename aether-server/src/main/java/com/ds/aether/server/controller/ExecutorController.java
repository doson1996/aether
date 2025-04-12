package com.ds.aether.server.controller;

import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.client.RegisterParam;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.service.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ds
 * @date 2025/4/10
 * @description 执行器相关api
 */
@Slf4j
@RestController
@RequestMapping(ServerConstant.EXECUTOR_PATH)
public class ExecutorController {

    @Resource
    private ExecutorService executorService;

    @PostMapping("exec-job")
    public Result<String> execJob(@RequestBody ExecJobParam param) {
        return executorService.execJob(param);
    }

    @GetMapping("list")
    public Result<Map<String, ExecutorInfo>> list() {
        return executorService.list();
    }

    @PostMapping("register")
    public Result<String> register(@RequestBody RegisterParam param) {
        return executorService.register(param);
    }

    @DeleteMapping("remove")
    public Result<String> remove(String name) {
        return executorService.remove(name);
    }

    @DeleteMapping("remove-all")
    public Result<String> removeAll() {
        return executorService.removeAll();
    }

}