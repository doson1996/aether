package com.ds.aether.server.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.common.Page;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;
import com.ds.aether.server.model.dto.BasePageParam;
import com.ds.aether.server.repo.MongoRepo;
import com.ds.aether.server.scheduler.SchedulerContext;
import com.ds.aether.server.service.JobInfoService;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2025/4/17
 * @description
 */
@Slf4j
@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Resource
    private MongoRepo mongoRepo;

    public static final String TABLE_NAME = "job_info";

    @Override
    public Result<String> register(Map<String, JSONObject> jobInfoMap) {
        jobInfoMap.forEach((jobName, jobInfo) -> {
            Bson condition = Filters.and(Filters.eq("jobName", jobName), Filters.eq("clientName", jobInfo.getString("clientName")));
            mongoRepo.saveOrUpdate(TABLE_NAME, condition, Document.parse(jobInfo.toJSONString()));
            String cron = jobInfo.getString("cron");
            if (StrUtil.isNotBlank(cron)) {
                SchedulerContext.schedule(cron, jobName);
            }
        });
        return Result.ok();
    }

    @Override
    public Result<String> add(AddJobParam param) {
        String jobName = param.getJobName();
        String appName = param.getAppName();
        Bson condition = Filters.and(Filters.eq("jobName", jobName), Filters.eq("appName", appName));
        param.setStatus(2);
        mongoRepo.saveOrUpdate(TABLE_NAME, condition, Document.parse(JSON.toJSONString(param)));

        String cronExpression = param.getCronExpression();
        if (StrUtil.isNotBlank(cronExpression)) {
            SchedulerContext.schedule(cronExpression, jobName);
        }
        return Result.ok();
    }

    @Override
    public Result<Page> page(BasePageParam param) {
        return Result.okData(mongoRepo.page(TABLE_NAME, null, null, null, param.getPageNum(), param.getPageSize(), true));
    }

}
