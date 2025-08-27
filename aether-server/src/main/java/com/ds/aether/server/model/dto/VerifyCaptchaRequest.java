package com.ds.aether.server.model.dto;

import lombok.Data;

/**
 * @author ds
 * @date 2023/4/17
 * @description
 */
@Data
public class VerifyCaptchaRequest {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String captcha;

}
