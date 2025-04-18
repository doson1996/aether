package com.ds.aether.server.service;

import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;

/**
 * @author ds
 * @date 2025/4/17
 * @description
 */
public interface JobInfoService {

    /**
     * 添加任务
     *
     * @param param
     * @return
     */
    Result<String> add(AddJobParam param);

}
