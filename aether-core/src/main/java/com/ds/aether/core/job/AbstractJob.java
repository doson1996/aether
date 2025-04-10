package com.ds.aether.core.job;

import com.ds.aether.core.model.JobResult;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
public abstract class AbstractJob {

    public abstract JobResult execute() throws Exception;

    /**
     *
     */
    public void work() {
        try {
            JobResult result = execute();
        } catch (Exception e) {
            // 异常上报
            exceptionReport(e);
        }
    }

    /**
     * todo 异常上报
     *
     * @param e
     */
    private void exceptionReport(Exception e) {

    }

}
