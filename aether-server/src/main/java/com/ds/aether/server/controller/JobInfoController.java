package com.ds.aether.server.controller;

import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.common.Page;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;
import com.ds.aether.server.model.dto.BasePageParam;
import com.ds.aether.server.model.vo.JobInfoVo;
import com.ds.aether.server.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return jobInfoService.register(jobInfoMap);
    }

    @PostMapping("add")
    public Result<String> add(@RequestBody AddJobParam param) {
        log.info("添加任务信息参数：{}", param);
        return jobInfoService.add(param);
    }

    /**
     * 获取任务列表
     *
     * @return 任务列表
     */
    @GetMapping("/page")
    public Result<Page> page(@RequestBody BasePageParam param) {
        log.info("获取任务列表：{}", param);
        return jobInfoService.page(param);
    }

}
