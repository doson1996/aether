package com.ds.aether.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ds
 * @date 2025/8/12
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportStateParam {

    public ReportStateParam(String jobId, String jobName, Integer status, String message) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.status = status;
        this.message = message;
    }

    public ReportStateParam(String jobId, String jobName, Integer status, String message, JobResult jobResult) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.status = status;
        this.message = message;
        this.jobResult = jobResult;
    }

    /**
     * 任务执行ID
     */
    private String jobId;

    private String jobName;

    private String executorName;

    private Integer status;

    private String message;

    private JobResult jobResult;

}
