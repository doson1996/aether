package com.ds.aether.server.model.dto;

import lombok.Data;

/**
 * @author ds
 * @date 2025/8/29
 * @description
 */
@Data
public class RegisterRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
