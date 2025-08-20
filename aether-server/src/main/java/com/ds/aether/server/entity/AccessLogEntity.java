package com.ds.aether.server.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author ds
 * @date 2025/8/20
 * @description
 */
@Data
public class AccessLogEntity {
    private Long id;
    private String userId;
    private String userIp;
    private String className;
    private String methodName;
    private String module;
    private String operation;
    private String params;
    private String result;
    private Long duration;
    private Boolean success;
    private String errorMsg;
    private String accessTime;
}
