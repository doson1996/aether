package com.ds.aether.core.job;

import lombok.Data;

/**
 * @author ds
 * @date 2025/4/10
 * @description 任务执行返回
 */
@Data
public class JobResult {

    /**
     * 是否执行成功
     */
    private boolean success;

    public JobResult(boolean success) {
        this.success = success;
    }

    public static JobResult success() {
        return new JobResult(true);
    }
}
