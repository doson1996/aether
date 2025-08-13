package com.ds.aether.core.client;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.model.HeartbeatParam;
import com.ds.aether.core.model.JobResult;
import com.ds.aether.core.model.ReportStateParam;
import com.ds.aether.core.model.client.RegisterParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/7/29
 * @description
 */
@Slf4j
public class ServerClient implements Client {

    private String serverHost;

    public ServerClient(String serverHost) {
        this.serverHost = serverHost;
    }

    public String sendHeartbeat(String clientName) {
        try {
            HeartbeatParam heartbeatParam = new HeartbeatParam();
            heartbeatParam.setName(clientName);
            // 执行器心跳请求地址
            String url = serverHost + ServerConstant.CLIENT_HEARTBEAT_PATH;
            return HttpUtil.post(url, JSONObject.toJSONString(heartbeatParam));
        } catch (Exception ex) {
            log.error("执行器{}发送心跳异常：", clientName, ex);
        }
        return "";
    }

    public String reportState(String jobName, String executorName, Integer status, String message, JobResult jobResult) {
        String url = serverHost + ServerConstant.JOB_INFO_REPORT_STATE_FULL_PATH;
        ReportStateParam reportStateParam = new ReportStateParam(jobName, executorName, status, message, jobResult);
        return HttpUtil.post(url, JSONObject.toJSONString(reportStateParam));
    }

    public String registerExecutor(String appName, String clientName, String clientHost, String clientPort, String contextPath) {
        try {
            RegisterParam registerParam = new RegisterParam();
            registerParam.setAppName(appName);
            registerParam.setName(clientName);
            registerParam.setHost(clientHost + ":" + clientPort);
            registerParam.setContextPath(contextPath);
            String registerExecutorUrl = serverHost + ServerConstant.EXECUTOR_REGISTER_PATH;
            return HttpUtil.post(registerExecutorUrl, JSONObject.toJSONString(registerParam));
        } catch (Exception ex) {
            log.error("注册执行器异常：", ex);
        }

        return "";
    }

    public String register(String path, String body) {
        String url = serverHost + ServerConstant.JOB_INFO_REGISTER_FULL_PATH;
        return HttpUtil.post(url, JSONObject.toJSONString(body));
    }

}
