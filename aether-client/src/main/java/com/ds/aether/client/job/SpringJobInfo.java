package com.ds.aether.client.job;

import com.ds.aether.core.context.SpringContext;
import com.ds.aether.core.model.ExecJobParam;
import lombok.Data;

/**
 * @author ds
 * @date 2025/4/11
 * @description
 */
@Data
public class SpringJobInfo implements JobInfo {

    public SpringJobInfo() {
    }

    public SpringJobInfo(String jobName, String beanName, String clientName) {
        this.jobName = jobName;
        this.beanName = beanName;
        this.clientName = clientName;
    }

    public String jobName;

    public String beanName;

    private String clientName;

    @Override
    public void execute(ExecJobParam execJobParam) {
        Object bean = SpringContext.getContext().getBean(beanName);
        if (bean instanceof AbstractJob) {
            AbstractJob job = (AbstractJob) bean;
            job.setParams(execJobParam.getParams());
            job.work(execJobParam);
        }
    }

}
