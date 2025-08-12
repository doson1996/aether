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
     * 任务类型 1.cron 2.定时任务 3.http 4.shell 5.java
     */
    private String jobType;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 类名
     */
    private String className;

    /**
     * http url
     */
    private String httpUrl;

    /**
     * 状态
     */
    private Integer status;

}
