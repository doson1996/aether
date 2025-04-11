package com.ds.aether.core.model;

import java.util.Map;

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
     * 任务参数（json）
     */
    private String params;

}
