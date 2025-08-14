package com.ds.aether.core.model.server;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorInfo {

    public ExecutorInfo(String name, String host, String contextPath) {
        this.name = name;
        this.host = host;
        this.contextPath = contextPath;
    }

    public ExecutorInfo(String name, String host, String contextPath, Integer status) {
        this.name = name;
        this.host = host;
        this.contextPath = contextPath;
        this.status = status;
    }

    public ExecutorInfo(String name, String host, String contextPath, Long lastHeartbeat, Integer status) {
        this.name = name;
        this.host = host;
        this.contextPath = contextPath;
        this.lastHeartbeat = lastHeartbeat;
        this.status = status;
    }

    /**
     * 执行器所属应用名称
     */
    private String appName;

    /**
     * 执行器名称
     */
    private String name;

    /**
     * 执行器地址
     */
    private String host;

    /**
     * 客户端请求上下文路径
     */
    private String contextPath;

    /**
     * 最后一次心跳时间
     */
    private Long lastHeartbeat;

    /**
     * 执行器状态 {@link com.ds.aether.core.constant.ExecutorStatus}
     */
    private Integer status;

    /**
     * 执行器权重
     */
    private Integer weight;

    /**
     * 注册时间
     */
    private String registerTime;

    /**
     * 是否禁用
     */
    private Integer disabled;

}
