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
     * 执行任务逻辑，留给子类覆写
     *
     * @return
     * @throws Exception
     */
    public abstract JobResult execute() throws Exception;

    /**
     *
     */
    public void work() {
        try {
            JobResult result = execute();
        } catch (Exception e) {
            log.error("任务【{}】执行发生异常：", currentJobName(), e);
            // 异常上报
            exceptionReport(e);
        } finally {
            log.info("任务【{}】执行完成", currentJobName());
        }
    }

    /**
     * todo 异常上报
     *
     * @param e
     */
    private void exceptionReport(Exception e) {

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

}
