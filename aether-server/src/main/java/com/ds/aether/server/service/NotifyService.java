package com.ds.aether.server.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author ds
 * @date 2025/8/27
 * @description
 */
public interface NotifyService {

    boolean sendMsg(JSONObject json);

    boolean sendMsg(String msg);

}
