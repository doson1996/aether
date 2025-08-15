package com.ds.aether.example.job;

import com.ds.aether.client.job.AbstractJob;
import com.ds.aether.client.job.Job;
import com.ds.aether.core.model.JobResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/4/10
 * @description 执行异常任务测试
 */
@Slf4j
@Job(name = "exJob")
public class ExJob extends AbstractJob {

    @Override
    public JobResult execute() throws Exception {
        String params = getParams();
        log.info("执行任务, params = {}", params);
        throw new RuntimeException("执行异常");
    }

}
