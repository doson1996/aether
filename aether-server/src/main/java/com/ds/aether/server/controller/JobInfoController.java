package com.ds.aether.server.controller;

import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.common.Page;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.ReportStateParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;
import com.ds.aether.server.model.dto.BasePageParam;
import com.ds.aether.server.service.ExecutorService;
import com.ds.aether.server.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Resource
    private ExecutorService executorService;

    @GetMapping("statistics")
    public Result<JSONObject> statistics() {
        return jobInfoService.statistics();
    }

    @PostMapping(ServerConstant.JOB_INFO_REGISTER_PATH)
    public Result<String> register(@RequestBody Map<String, JSONObject> jobInfoMap) {
        log.info("任务信息：{}", jobInfoMap);
        return jobInfoService.register(jobInfoMap);
    }

    @PostMapping(ServerConstant.JOB_INFO_REPORT_STATE_PATH)
    public Result<String> reportState(@RequestBody ReportStateParam param) {
        log.info("上报任务状态：{}", param);
        return jobInfoService.reportState(param);
    }

    @PostMapping("saveOrUpdate")
    public Result<String> saveOrUpdate(@RequestBody AddJobParam param) {
        log.info("添加任务信息参数：{}", param);
        return jobInfoService.saveOrUpdate(param);
    }

    @PostMapping("start/{jobName}")
    public Result<String> start(@PathVariable String jobName) {
        ExecJobParam execJobParam = new ExecJobParam();
        execJobParam.setJobName(jobName);

        // 携带任务参数
        Document jobInfo = jobInfoService.findOne(jobName);
        execJobParam.setParams(jobInfo.getString("jobParams"));
        return executorService.execJob(execJobParam);
    }

    @PostMapping("schedule/{jobName}")
    public Result<String> schedule(@PathVariable String jobName) {
        return jobInfoService.schedule(jobName);
    }

    @PostMapping("cancel/{jobName}")
    public Result<String> cancel(@PathVariable String jobName) {
        return jobInfoService.cancel(jobName);
    }

    @DeleteMapping("delete/{jobName}")
    public Result<String> delete(@PathVariable String jobName) {
        log.info("删除任务信息参数：{}", jobName);
        return jobInfoService.delete(jobName);
    }

    @GetMapping("detail/{jobName}")
    public Result<JSONObject> detail(@PathVariable String jobName) {
        log.info("任务详情参数：{}", jobName);
        return jobInfoService.detail(jobName);
    }

    /**
     * 获取任务列表
     *
     * @return 任务列表
     */
    @PostMapping("/page")
    public Result<Page> page(@RequestBody BasePageParam param) {
        log.info("获取任务列表：{}", param);
        return jobInfoService.page(param);
    }

}
