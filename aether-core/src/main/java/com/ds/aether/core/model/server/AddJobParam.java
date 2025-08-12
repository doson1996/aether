package com.ds.aether.core.model.server;

import lombok.Data;

/**
 * @author ds
 * @date 2025/4/18
 * @description
 */
@Data
public class AddJobParam {

    /**
     * 应用名
     */
    private String appName;

    /**
     * 任务名
     */
    private String jobName;

    /**
     * 任务描述
     */
    private String jobDescription;

    /**
     * cron表达式
     */
    private String cronExpression;

}
