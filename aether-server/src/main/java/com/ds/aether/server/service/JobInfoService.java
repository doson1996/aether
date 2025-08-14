package com.ds.aether.server.service;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.common.Page;
import com.ds.aether.core.model.ReportStateParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.server.AddJobParam;
import com.ds.aether.server.model.dto.BasePageParam;
import org.bson.Document;

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
    Result<String> saveOrUpdate(AddJobParam param);

    /**
     * 获取任务列表
     *
     * @param param
     * @return
     */
    Result<Page> page(BasePageParam param);

    /**
     * 删除任务
     *
     * @param jobName
     * @return
     */
    Result<String> delete(String jobName);

    /**
     * 查询任务
     *
     * @param jobName
     * @return
     */
    Document findOne(String jobName);

    /**
     * 上报任务状态
     *
     * @param param
     * @return
     */
    Result<String> reportState(ReportStateParam param);

    /**
     * 调度任务
     *
     * @param jobName
     * @return
     */
    Result<String> schedule(String jobName);

    /**
     * 取消任务
     *
     * @param jobName
     * @return
     */
    Result<String> cancel(String jobName);

    Result<JSONObject> detail(String jobName);
}
