package com.ds.aether.client.api;

import cn.hutool.core.util.StrUtil;
import com.ds.aether.client.executor.Executor;
import com.ds.aether.core.constant.ClientConstant;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ds
 * @date 2025/4/10
 * @description 客户端相关api
 */
@RestController
@RequestMapping(ClientConstant.CLIENT_PATH)
public class ClientController {

    @Resource
    private Executor executor;

    @PostMapping("exec-job")
    public Result<String> execJob(@RequestBody ExecJobParam param) {
        String jobName = param.getJobName();
        if (StrUtil.isBlank(jobName)) {
            return Result.fail("任务名不能为空!");
        }

        executor.executeJob(jobName);
        return Result.ok("执行任务【" + jobName + "】成功!");
    }

}
