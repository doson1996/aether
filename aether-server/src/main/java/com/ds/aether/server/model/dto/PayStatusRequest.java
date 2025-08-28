package com.ds.aether.server.model.dto;

import lombok.Data;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
@Data
public class PayStatusRequest {

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 支付宝交易号
     */
    private String tradeNo;

}
