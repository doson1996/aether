package com.ds.aether.core.client;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.constant.ServerConstant;

/**
 * @author ds
 * @date 2025/7/29
 * @description
 */
public class ServerClient implements Client {

    private String serverHost;

    public ServerClient(String serverHost) {
        this.serverHost = serverHost;
    }

    public String post(String path, String body) {
        String url = serverHost + ServerConstant.JOB_INFO_REGISTER_FULL_PATH;
        return HttpUtil.post(url, JSONObject.toJSONString(body));
    }

}
