package com.ds.aether.core.model;

import lombok.Data;

/**
 * @author ds
 * @date 2025/4/10
 * @description 执行任务参数
 */
@Data
public class ExecJobParam {

    /**
     * 任务名
     */
    private String jobName;

    /**
     * 任务类型
     */
    private String jobType;

    /**
     * 任务参数（json）
     */
    private String params;

    /**
     * todo 执行类型 0.随机选择执行器执行 1.指定执行器执行
     */
    private Integer type;

    /**
     * 执行器名
     */
    private String executorName;

}
