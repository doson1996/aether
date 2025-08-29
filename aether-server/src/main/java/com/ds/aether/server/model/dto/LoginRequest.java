package com.ds.aether.server.model.dto;

import lombok.Data;

/**
 * @author ds
 * @date 2025/8/29
 * @description 登录请求
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
