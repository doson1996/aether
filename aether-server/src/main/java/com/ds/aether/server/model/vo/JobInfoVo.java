package com.ds.aether.server.model.vo;

import lombok.Data;

/**
 * @author ds
 * @date 2025/8/11
 * @description
 */
@Data
public class JobInfoVo {

    private String jobName;

    private String jobDescription;

    private String className;

    private String cronExpression;

    /**
     * 0-暂停, 1-运行中
     */
    private Integer status;

}
