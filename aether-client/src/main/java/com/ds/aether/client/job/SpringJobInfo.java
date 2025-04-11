package com.ds.aether.client.job;

import com.ds.aether.core.context.SpringContext;
import com.ds.aether.core.job.AbstractJob;
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

    public String jobName;

    public String beanName;

    @Override
    public void execute() {
        Object bean = SpringContext.getContext().getBean(beanName);
        if (bean instanceof AbstractJob) {
            ((AbstractJob) bean).work();
        }

    }
}
