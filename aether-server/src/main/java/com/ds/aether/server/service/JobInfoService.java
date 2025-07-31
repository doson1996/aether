package com.ds.aether.server.service;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;

/**
 * @author ds
 * @date 2025/4/17
 * @description
 */
public interface JobInfoService {

    /**
     * 注册任务
     *
     * @param jobInfoMap
     * @return
     */
    Result<String> register(Map<String, JSONObject> jobInfoMap);

    /**
     * 添加任务
     *
     * @param param
     * @return
     */
    Result<String> add(AddJobParam param);

}
