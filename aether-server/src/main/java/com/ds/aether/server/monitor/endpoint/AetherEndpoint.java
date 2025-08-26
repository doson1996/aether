package com.ds.aether.server.monitor.endpoint;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.ds.aether.server.service.JobInfoService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/8/26
 * @description
 */
@Endpoint(id = "aether")
@Component
public class AetherEndpoint {

    @Resource
    private JobInfoService jobInfoService;

    @ReadOperation
    public Map<String, Object> endpoint() {
        Map<String, Object> data = new HashMap<>();
        data.put("jobs", jobInfoService.statistics().getData());
        return data;
    }

}
