package com.ds.aether.core.model.client;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ds
 * @date 2025/4/10
 * @description
 */
@Data
public class RegisterParam implements Serializable {

    private static final long serialVersionUID = 1350156985574491056L;

    /**
     * 执行器所属名称
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
     * 请求执行器上下文路径
     */
    private String contextPath;

}
