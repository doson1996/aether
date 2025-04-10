package com.ds.aether.client.api;

import javax.annotation.Resource;

import com.ds.aether.client.executor.Executor;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.Result;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@RestController
@RequestMapping("client")
public class ClientController {

    @Resource
    private Executor executor;

    @DeleteMapping("exec-job")
    public Result<String> execJob(ExecJobParam param) {
        String jobName = param.getJobName();
        executor.executeJob(jobName);
        return Result.ok("执行任务【" + jobName + "】成功!");
    }

}
