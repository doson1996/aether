package com.ds.aether.client.job;

import com.ds.aether.core.model.ExecJobParam;

/**
 * @author ds
 * @date 2025/4/11
 * @description
 */
public interface JobInfo {

    void execute(ExecJobParam execJobParam);

}
