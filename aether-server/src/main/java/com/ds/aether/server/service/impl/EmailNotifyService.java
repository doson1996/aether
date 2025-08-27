package com.ds.aether.server.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.server.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2025/8/27
 * @description
 */
@Slf4j
@Service
public class EmailNotifyService implements NotifyService {

    @Override
    public boolean sendMsg(JSONObject json) {
        return false;
    }

    @Override
    public boolean sendMsg(String msg) {
        return false;
    }

}
