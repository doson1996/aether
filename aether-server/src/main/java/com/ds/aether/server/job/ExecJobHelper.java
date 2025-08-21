package com.ds.aether.server.job;

import javax.annotation.Resource;

import cn.hutool.http.HttpUtil;
import com.ds.aether.core.constant.JobType;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.server.service.ExecutorService;
import com.ds.aether.server.service.JobInfoService;
import org.bson.Document;
import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/8/14
 * @description
 */
@Component
public class ExecJobHelper {

    @Resource
    private JobInfoService jobInfoService;

    @Resource
    private ExecutorService executorService;

    public Result<String> start(String jobName) {
        ExecJobParam execJobParam = new ExecJobParam();
        execJobParam.setJobName(jobName);

        // 携带任务参数
        Document jobInfo = jobInfoService.findOne(jobName);
        execJobParam.setParams(jobInfo.getString("jobParams"));
        String jobType = jobInfo.getString("jobType");
        execJobParam.setJobType(jobType);
        if (JobType.CRON.equals(jobType)) {
            return executorService.execJob(execJobParam);
        }

        if (JobType.HTTP.equals(jobType)) {
            String httpUrl = jobInfo.getString("httpUrl");
            HttpUtil.get(httpUrl);
            return Result.ok();
        }

        return Result.fail("任务类型错误");
    }

}
