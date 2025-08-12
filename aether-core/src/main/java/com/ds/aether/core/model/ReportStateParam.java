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

    public ReportStateParam(String jobName, Integer status, String message) {
        this.jobName = jobName;
        this.status = status;
        this.message = message;
    }

    public ReportStateParam(String jobName, Integer status, String message, JobResult jobResult) {
        this.jobName = jobName;
        this.status = status;
        this.message = message;
        this.jobResult = jobResult;
    }

    private String jobName;

    private String executorName;

    private Integer status;

    private String message;

    private JobResult jobResult;

}
