package com.ds.aether.server.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;
import com.ds.aether.server.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ds
 * @date 2025/4/14
 * @description 服务端任务信息相关api
 */
@Slf4j
@RestController
@RequestMapping(ServerConstant.JOB_INFO_PATH)
public class JobInfoController {

    @Resource
    private JobInfoService jobInfoService;

    @PostMapping(ServerConstant.JOB_INFO_REGISTER_PATH)
    public Result<String> register(@RequestBody Map<String, JSONObject> jobInfoMap) {
        log.info("任务信息：{}", jobInfoMap);
        return Result.ok();
    }

    @PostMapping("add")
    public Result<String> add(@RequestBody AddJobParam param) {
        log.info("添加任务信息参数：{}", param);
        return jobInfoService.add(param);
    }

}
