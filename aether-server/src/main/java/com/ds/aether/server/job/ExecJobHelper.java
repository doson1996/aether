package com.ds.aether.server.job;

import javax.annotation.Resource;

import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.server.service.ExecutorService;
import com.ds.aether.server.service.JobInfoService;
import org.bson.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

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

    public Result<String> start(@PathVariable String jobName) {
        ExecJobParam execJobParam = new ExecJobParam();
        execJobParam.setJobName(jobName);

        // 携带任务参数
        Document jobInfo = jobInfoService.findOne(jobName);
        execJobParam.setParams(jobInfo.getString("jobParams"));
        return executorService.execJob(execJobParam);
    }

}
