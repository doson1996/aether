package com.ds.aether.server.pay;

/**
 * @author ds
 * @date 2025/8/28
 * @description 支付状态
 */
public class PaymentStatus {

    /**
     * 支付成功
     */
    public static final String SUCCESS = "TRADE_SUCCESS";

    /**
     * 支付关闭
     */
    public static final String CLOSED = "TRADE_CLOSED";

    /**
     * 待支付
     */
    public static final String WAIT = "WAIT_BUYER_PAY";

    /**
     * 交易结束，不可退款
     */
    public static final String FINISHED = "TRADE_FINISHED";

}
