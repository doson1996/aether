package com.ds.aether.server.service;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.common.Page;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;
import com.ds.aether.server.model.dto.BasePageParam;
import com.ds.aether.server.model.vo.JobInfoVo;

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

    /**
     * 获取任务列表
     *
     * @param param
     * @return
     */
    Result<Page> page(BasePageParam param);

}
