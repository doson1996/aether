package com.ds.aether.core.job;

import com.ds.aether.core.model.JobResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Slf4j
public abstract class AbstractJob {

    /**
     * 任务参数
     */
    private String params;

    /**
     * 任务执行结果
     */
    private JobResult jobResult;

    /**
     * 执行任务逻辑，留给子类覆写
     *
     * @return
     * @throws Exception
     */
    public abstract JobResult execute() throws Exception;

    /**
     * 执行任务
     */
    public void work() {
        try {
            JobResult result = execute();
            // 设置任务执行结果
            setJobResult(result);
        } catch (Exception e) {
            log.error("任务【{}】执行发生异常：", currentJobName(), e);
            // 异常上报
            exceptionReport(currentJobName(), e);
        } finally {
            log.info("任务【{}】执行完成", currentJobName());
        }
    }

    /**
     * todo 异常上报
     *
     * @param e
     */
    private void exceptionReport(String jobName, Exception e) {

    }

    /**
     * 当前任务名称
     *
     * @return
     */
    private String currentJobName() {
        Job annotation = this.getClass().getAnnotation(Job.class);
        return annotation == null ? null : annotation.name();
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    public JobResult getJobResult() {
        return jobResult;
    }

    public void setJobResult(JobResult jobResult) {
        this.jobResult = jobResult;
    }

}
