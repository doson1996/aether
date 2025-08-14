package com.ds.aether.server.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.common.Page;
import com.ds.aether.core.constant.JobType;
import com.ds.aether.core.constant.YesOrNo;
import com.ds.aether.core.job.JobState;
import com.ds.aether.core.model.ReportStateParam;
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
import org.springframework.util.CollectionUtils;

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

    @Resource
    private SchedulerContext schedulerContext;

    public static final String TABLE_NAME = "job_info";

    @Override
    public Result<String> register(Map<String, JSONObject> jobInfoMap) {
        jobInfoMap.forEach((jobName, jobInfo) -> {
            Bson condition = Filters.and(Filters.eq("jobName", jobName), Filters.eq("clientName", jobInfo.getString("clientName")));
            mongoRepo.saveOrUpdate(TABLE_NAME, condition, Document.parse(jobInfo.toJSONString()));
        });
        return Result.ok();
    }

    @Override
    public Result<String> saveOrUpdate(AddJobParam param) {
        String jobName = param.getJobName();
        String appName = param.getAppName();
        Bson condition = Filters.and(Filters.eq("jobName", jobName), Filters.eq("appName", appName));
        mongoRepo.saveOrUpdate(TABLE_NAME, condition, Document.parse(JSON.toJSONString(param)));
        return Result.ok("添加成功!");
    }

    @Override
    public Result<Page> page(BasePageParam param) {
        Page page = mongoRepo.page(TABLE_NAME, null, null, null, param.getPageNum(), param.getPageSize(), true);
        List<Document> data = (List<Document>) page.getData();
        for (Document datum : data) {
            String jobName = datum.getString("jobName");
            if (StrUtil.isNotBlank(jobName)) {
                datum.put("scheduling", schedulerContext.isScheduled(jobName) ? YesOrNo.YES : YesOrNo.NO);
            }
        }
        return Result.okData(page);
    }

    @Override
    public Result<String> delete(String jobName) {
        if (StrUtil.isBlank(jobName)) {
            return Result.fail("任务名不能为空!");
        }
        Bson condition = Filters.and(Filters.eq("jobName", jobName));
        mongoRepo.deleteMany(TABLE_NAME, condition);
        return Result.ok("删除成功!");
    }

    @Override
    public Document findOne(String jobName) {
        Bson condition = Filters.and(Filters.eq("jobName", jobName));
        return mongoRepo.findOne(TABLE_NAME, condition);
    }

    @Override
    public Result<String> schedule(String jobName) {
        Document jobInfo = findOne(jobName);
        if (CollectionUtils.isEmpty(jobInfo)) {
            return Result.fail("任务不存在!");
        }

        String jobType = jobInfo.getString("jobType");
        if (JobType.CRON.equals(jobType)) {
            String cronExpression = jobInfo.getString("cronExpression");
            if (StrUtil.isBlank(cronExpression)) {
                return Result.fail("请先设置cron表达式");
            }

            if (schedulerContext.isScheduled(jobName)) {
                return Result.fail("任务正在调度中!");
            }

            schedulerContext.schedule(cronExpression, jobName);
            return Result.ok("任务调度成功!");
        }
        return Result.fail("非cron类型任务不允许调度");
    }

    @Override
    public Result<String> cancel(String jobName) {
        if (StrUtil.isBlank(jobName)) {
            return Result.fail("任务名不能为空!");
        }

        boolean cancel = schedulerContext.cancel(jobName);
        return cancel ? Result.ok("取消成功") : Result.fail("取消失败");
    }

    @Override
    public Result<JSONObject> detail(String jobName) {
        if (StrUtil.isBlank(jobName)) {
            return Result.fail("任务名不能为空!");
        }

        Document document = mongoRepo.findOne(TABLE_NAME, Filters.eq("jobName", jobName));
        if (CollectionUtils.isEmpty(document)) {
            return Result.fail("任务不存在");
        }

        return Result.okData(JSON.parseObject(document.toJson()));
    }

    @Override
    public Result<JSONObject> statistics() {
        JSONObject result = new JSONObject();
        result.put("total", mongoRepo.count(TABLE_NAME, null));
        result.put("notExecuted", mongoRepo.count(TABLE_NAME, Filters.eq("status", JobState.NOT_EXECUTED)));
        result.put("completed", mongoRepo.count(TABLE_NAME, Filters.eq("status", JobState.COMPLETED)));
        result.put("running", mongoRepo.count(TABLE_NAME, Filters.eq("status", JobState.RUNNING)));
        result.put("error", mongoRepo.count(TABLE_NAME, Filters.eq("status", JobState.ERROR)));
        result.put("scheduled", schedulerContext.getScheduledTaskCount());

        return null;
    }

    @Override
    public Result<String> reportState(ReportStateParam param) {
        String jobName = param.getJobName();

        if (StrUtil.isBlank(jobName)) {
            return Result.fail("任务名不能为空!");
        }

        Integer status = param.getStatus();
        Document updateDoc = new Document();
        updateDoc.put("status", status);
        mongoRepo.updateOne(TABLE_NAME, Filters.and(Filters.eq("jobName", jobName)), new Document("$set", updateDoc));

        // 任务上报记录
        Document log = Document.parse(JSON.toJSONString(param));
        log.put("reportTime", DateUtil.now());
        mongoRepo.insert("job_exec_log", log);
        return Result.ok("OK");
    }

}
