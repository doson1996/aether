package com.ds.aether.server.pay;

import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ds
 * @date 2025/8/26
 * @description
 */
@Slf4j
public class AliPayUtil {

    /**
     * 验签
     *
     * @param params
     * @param alipayConfig
     * @return
     */
    public static Boolean check(Map<String, String> params, AlipayConfig alipayConfig) {
        // 调用SDK验证签名
        try {
            return AlipaySignature.rsaCheckV1(params,
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(),
                    alipayConfig.getSignType());
        } catch (AlipayApiException e) {
            log.error("支付验签异常：", e);
        }

        return Boolean.FALSE;
    }

}
