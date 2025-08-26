package com.ds.aether.server.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2025/8/26
 * @description
 */
@Service
public class AlipaySandboxTradePagePay {
    // 沙箱环境网关地址
    private static final String SANDBOX_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 沙箱环境配置信息（实际使用时应从配置文件读取）
    // 沙箱应用APPID
    private static final String APP_ID = "9021000151646079";
    // 应用私钥
    private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDTrF1b2L4G3ezWk+SmprCAFYMpItsYmkN9riUiumzAWATlekO6oP88FctCm8b4l93lzUsX5d0kGLDSmqAYy4dz0TBXfmy6bEEZSyZ80AMifz7yFNMTaIMB3rtKhzkflfaRLCBKfVcWugMsRyq7WuB2rQEFd9Jql3Agld+NBno4nvRMy/7MVXS5YR5W08VPzLl8Wzgqy2yLxZCEAH8mYqEfS8IDIXglpG93qcgjWVwXoTiis1MqZHlllpqUbHgaJ5PpRCP32Vw4Mx/t8VaGVpUfuw2ckb6eCx3a8yZHMXvEL/XuMe0zjZ48zrT5nJiSoWTYBr3gZRKSqes0ihdbazdxAgMBAAECggEBAMILfh5HyJRk98wSEgeQQbd5gR7B3FZmfL6HWXXHrxB44Cw6dEJveuvrEsXCJpoYJoyXMWL8D0ka9WZr554zXr4WpBlhRW109gyo7uR2kJkcS2kDHCjro0WgmNOOuRgykrGs4QFIfADSjzwVFaBhg+pnWMtZd+TNZTYKDsCqknq52myz1MAwKfBTykp76AdldsfgzTEhdoPUuK+YmclgJESPcLxAuatN5PAwGVEXE3scmM4sFQwl2sabasL+M0SQ1avfrkvDexZ7tnsZitD1Gwmb+AKzxSYQTG5bQeDwht3+uI6za5iX4JM+Zslf2/W45c6eHMafV84LDU8kBMUAe80CgYEA6qd352ANVXNn3Jo5l8eOiellJwQ3xOE3ESJ12/etEyZ5ZfRecHBHAP7whDQSYbW6xC4X969g/jrtHZOwZ4B8ETrquTxbZs/na04gTyvgf4MIdqvad9rOcmRo2LxY/LgNSxhoDHn76crk+oSkvhfuCCTrj7awuqRNLbLg/McCfZsCgYEA5u26HK5YczJvARHcY2LrEI1Xt6J8SfBUlm6Wcov/LtHQICPW3ZvtpnP2xG4nHZ7c4XM1sAcFAK0lxwhf9xt8o0mznlYN5LrTIFg2sHoQ29m2LuzQUKUN1IP6B/MwTxdcXSrDPfpguBMrI8LnhySyMlNTlQ5Gidszbpy5s3ZKdeMCgYEA5MvniK3KMoB1S88AyvJkFCqDW3isXAZwp/9exd0IX7zK79NG5gFD6j+qCm2vYqBMfA6phfL18s1H9+fSQAkyb6Zvya+FO4kKD0G9FRUmL453CSblvKmXVEh9Rp7XcYqQQ6GHimrCayJPA8mjzEoO6Nf+60DprwKW0jExWKc+0XMCgYAMsTAzd1mhKzpyopqsU7l7tWkGzMVsAuuDQRy/uvYRirKXsaCTmYhcR69eaHd550tYkM54mEosGVgkMk+j8zzMdLaMk0o8MhB8jJyk4nCexL6Aob9pT0kNTxuk6DbVggEvlbQZafN66oLKemHjSZ8JqQ7E+H3kAco34CFMvcml5QKBgEEU8iXkBEdFYq+l8mtihVUT5xG0vGpvawQGH18S1AGmSH5++fsHm3rTk26hsZDB0qM+nLWvJ/ioXJRQhhKpHf6y6x8mWdckC8S+2vw3jQvM3TrwMBwusMb+Cz1xEqHp2hx4ib27KGJAVY6D5iDqY2SwjrGQkqdjoz0nUUF3HdwX";
    // 支付宝公钥
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnaVEs2U8XFCglT1cyN/h8sq5hfu38FdyARvVNHY9sOdMqvdIuEymRYvFSkje5IROzs2ul6AzPj3CmrVNdfFUf7dPMdEAlVfuxz3MSd64ReQIz07BqBf5jfIm7aEWniMv/Dv5PMrQghrlGqYGrn/KXddcOKhLQ2MSWl2jwLUfhcSJiyiCXfKmZ39b4d9yfpzFjzuXqe8py0qz08fx97/4Ni1+Hl5C5T/uZZsFwCEFfD3rNveWL9DWo/6hnRQn/ZOUO6djc7qp2oMkQu9etpyI/moO123CYzJ7Ggu6N959DPD67+NyqFtKEsSdwDfgOvf0WOPMA4Q5Lb3aM5foZoLuywIDAQAB";
    private static final String CHARSET = "UTF-8";
    private static final String FORMAT = "json";
    private static final String SIGN_TYPE = "RSA2";

    /**
     * 创建支付页面
     * @param outTradeNo 商户订单号
     * @param totalAmount 订单总金额
     * @param subject 订单标题
     * @param body 订单描述
     * @return 支付页面HTML
     * @throws AlipayApiException
     */
    public String createPayment(String outTradeNo, String totalAmount,
                                String subject, String body) throws AlipayApiException {
        // 初始化沙箱环境SDK客户端
        AlipayClient alipayClient = new DefaultAlipayClient(getSandboxAlipayConfig());

        // 构造请求参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 同步回调地址
        request.setReturnUrl("http://localhost:23843/api/alipay/return");
        // 异步通知地址
        request.setNotifyUrl("http://localhost:23843/api/alipay/notify");

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(totalAmount);
        model.setSubject(subject);
        model.setBody(body);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        request.setBizModel(model);

        // 执行支付请求
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        return response.getBody();
    }

    /**
     * 获取沙箱环境配置
     * @return AlipayConfig
     */
    public AlipayConfig getSandboxAlipayConfig() {
        AlipayConfig alipayConfig = new AlipayConfig();
        // 沙箱环境网关
        alipayConfig.setServerUrl(SANDBOX_URL);
        // 沙箱应用ID
        alipayConfig.setAppId(APP_ID);
        // 应用私钥
        alipayConfig.setPrivateKey(PRIVATE_KEY);
        // 数据格式
        alipayConfig.setFormat(FORMAT);
        // 支付宝公钥
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        // 字符集
        alipayConfig.setCharset(CHARSET);
        // 签名类型
        alipayConfig.setSignType(SIGN_TYPE);
        return alipayConfig;
    }

}
