package com.ds.aether.example.job;

import com.ds.aether.client.job.AbstractJob;
import com.ds.aether.client.job.Job;
import com.ds.aether.core.model.JobResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/4/10
 * @description 示例任务
 * 使用示例
 * 1.添加@Job注解，指定任务名称
 * 2.继承AbstractJob，实现execute方法，执行任务逻辑
 */
@Slf4j
@Job(name = "job1")
public class ExampleJob extends AbstractJob {

    @Override
    public JobResult execute() throws Exception {
        String params = getParams();
        log.info("执行任务, params = {}", params);
        return JobResult.success();
    }

}
