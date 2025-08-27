package com.ds.aether.server.model.dto;

import lombok.Data;

/**
 * @author ds
 * @date 2023/4/20
 * @description 发送邮件验证码入参
 */
@Data
public class SendCaptchaRequest {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 收件人
     */
    private String to;

    /**
     * 邮件标题
     */
    private String subject;

    /**
     * 本次验证码进行的操作
     */
    private String operate;

    /**
     * 验证码有效期(分钟)
     */
    private Integer expire;

}
