package com.ds.aether.server.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(ServerConstant.JOB_INFO_REGISTER_PATH)
    public Result<String> register(@RequestBody Map<String, JSONObject> jobInfoMap) {
        log.info("任务信息：{}", jobInfoMap);
        return Result.ok();
    }

}
