package com.ds.aether.client.job;

import com.ds.aether.core.context.SpringContext;
import com.ds.aether.core.job.AbstractJob;
import com.ds.aether.core.job.JobInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ds
 * @date 2025/4/11
 * @description
 */
@Data
@AllArgsConstructor
public class SpringJobInfo implements JobInfo {

    public SpringJobInfo(String jobName, String beanName, String clientName, String cron) {
        this.jobName = jobName;
        this.beanName = beanName;
        this.clientName = clientName;
        this.cron = cron;
    }

    public String jobName;

    public String beanName;

    private String clientName;

    private String methodName;

    private String cron;

    @Override
    public void execute(String params) {
        Object bean = SpringContext.getContext().getBean(beanName);
        if (bean instanceof AbstractJob) {
            AbstractJob job = (AbstractJob) bean;
            job.setParams(params);
            job.work();
        }

    }
}
