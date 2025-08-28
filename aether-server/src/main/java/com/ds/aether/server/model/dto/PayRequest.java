package com.ds.aether.server.model.dto;

import lombok.Data;

/**
 * @author ds
 * @date 2025/8/28
 * @description 支付参数
 */
@Data
public class PayRequest {

    /**
     * 支付方式 0.支付宝 1.微信
     */
    private String payType;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单金额
     */
    private String totalAmount;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 订单描述
     */
    private String body;

    /**
     * 订单过期时间
     */
    private String timeExpire;

}
