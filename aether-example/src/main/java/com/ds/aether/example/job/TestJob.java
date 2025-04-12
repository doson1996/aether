package com.ds.aether.example.job;

import com.ds.aether.core.job.AbstractJob;
import com.ds.aether.core.job.Job;
import com.ds.aether.core.model.JobResult;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Job(name = "job1")
public class TestJob extends AbstractJob {

    @Override
    public JobResult execute() throws Exception {
        System.out.println("true = " + true);
        return JobResult.success();
    }

}
