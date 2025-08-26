package com.ds.aether.server.pay;

import java.util.ArrayList;
import java.util.List;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.ExtUserInfo;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.domain.InvoiceInfo;
import com.alipay.api.domain.InvoiceKeyInfo;
import com.alipay.api.domain.SubMerchant;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;

/**
 *
 *
 * @author ds
 * @date 2025/8/26
 * @description 实现支付宝沙箱环境下的支付功能
 */
public class AlipayTradePagePay {

    // 沙箱环境网关地址
    private static final String SANDBOX_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    private static final String URL = "https://openapi.alipay.com/gateway.do";


    public static void main(String[] args) throws AlipayApiException {
        // 初始化SDK
        AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());

        // 构造请求参数以调用接口
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        // 设置商户订单号
        model.setOutTradeNo("20150320010101001");

        // 设置订单总金额
        model.setTotalAmount("88.88");

        // 设置订单标题
        model.setSubject("Iphone6 16G");

        // 设置产品码
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        // 设置PC扫码支付的方式
        model.setQrPayMode("1");

        // 设置商户自定义二维码宽度
        model.setQrcodeWidth(100L);

        // 设置订单包含的商品列表信息
        List<GoodsDetail> goodsDetail = new ArrayList<GoodsDetail>();
        GoodsDetail goodsDetail0 = new GoodsDetail();
        goodsDetail0.setGoodsName("ipad");
        goodsDetail0.setAlipayGoodsId("20010001");
        goodsDetail0.setQuantity(1L);
        goodsDetail0.setPrice("2000");
        goodsDetail0.setGoodsId("apple-01");
        goodsDetail0.setGoodsCategory("34543238");
        goodsDetail0.setCategoriesTree("124868003|126232002|126252004");
        goodsDetail0.setShowUrl("http://www.alipay.com/xxx.jpg");
        goodsDetail.add(goodsDetail0);
        model.setGoodsDetail(goodsDetail);

        // 设置订单绝对超时时间
        model.setTimeExpire("2016-12-31 10:05:01");

        // 设置二级商户信息
        SubMerchant subMerchant = new SubMerchant();
        subMerchant.setMerchantId("2088000603999128");
        subMerchant.setMerchantType("alipay");
        model.setSubMerchant(subMerchant);

        // 设置业务扩展参数
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088511833207846");
        extendParams.setHbFqSellerPercent("100");
        extendParams.setHbFqNum("3");
        extendParams.setIndustryRefluxInfo("{\"scene_code\":\"metro_tradeorder\",\"channel\":\"xxxx\",\"scene_data\":{\"asset_name\":\"ALIPAY\"}}");
        extendParams.setRoyaltyFreeze("true");
        extendParams.setCardType("S0JP0000");
        model.setExtendParams(extendParams);

        // 设置商户传入业务信息
        model.setBusinessParams("{\"mc_create_trade_ip\":\"127.0.0.1\"}");

        // 设置优惠参数
        model.setPromoParams("{\"storeIdType\":\"1\"}");

        // 设置请求后页面的集成方式
        model.setIntegrationType("PCWEB");

        // 设置请求来源地址
        model.setRequestFromUrl("https://");

        // 设置商户门店编号
        model.setStoreId("NJ_001");

        // 设置商户的原始订单号
        model.setMerchantOrderNo("20161008001");

        // 设置外部指定买家
        ExtUserInfo extUserInfo = new ExtUserInfo();
        extUserInfo.setCertType("IDENTITY_CARD");
        extUserInfo.setCertNo("362334768769238881");
        extUserInfo.setMobile("16587658765");
        extUserInfo.setName("李明");
        extUserInfo.setMinAge("18");
        extUserInfo.setNeedCheckInfo("F");
        extUserInfo.setIdentityHash("27bfcd1dee4f22c8fe8a2374af9b660419d1361b1c207e9b41a754a113f38fcc");
        model.setExtUserInfo(extUserInfo);

        // 设置开票信息
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        InvoiceKeyInfo keyInfo = new InvoiceKeyInfo();
        keyInfo.setTaxNum("1464888883494");
        keyInfo.setIsSupportInvoice(true);
        keyInfo.setInvoiceMerchantName("ABC|003");
        invoiceInfo.setKeyInfo(keyInfo);
        invoiceInfo.setDetails("[{\"code\":\"100294400\",\"name\":\"服饰\",\"num\":\"2\",\"sumPrice\":\"200.00\",\"taxRate\":\"6%\"}]");
        model.setInvoiceInfo(invoiceInfo);

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        // 如果需要返回GET请求，请使用
        // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
        String pageRedirectionData = response.getBody();
        System.out.println(pageRedirectionData);

        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            // System.out.println(diagnosisUrl);
        }
    }

    private static AlipayConfig getAlipayConfig() {
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDTrF1b2L4G3ezWk+SmprCAFYMpItsYmkN9riUiumzAWATlekO6oP88FctCm8b4l93lzUsX5d0kGLDSmqAYy4dz0TBXfmy6bEEZSyZ80AMifz7yFNMTaIMB3rtKhzkflfaRLCBKfVcWugMsRyq7WuB2rQEFd9Jql3Agld+NBno4nvRMy/7MVXS5YR5W08VPzLl8Wzgqy2yLxZCEAH8mYqEfS8IDIXglpG93qcgjWVwXoTiis1MqZHlllpqUbHgaJ5PpRCP32Vw4Mx/t8VaGVpUfuw2ckb6eCx3a8yZHMXvEL/XuMe0zjZ48zrT5nJiSoWTYBr3gZRKSqes0ihdbazdxAgMBAAECggEBAMILfh5HyJRk98wSEgeQQbd5gR7B3FZmfL6HWXXHrxB44Cw6dEJveuvrEsXCJpoYJoyXMWL8D0ka9WZr554zXr4WpBlhRW109gyo7uR2kJkcS2kDHCjro0WgmNOOuRgykrGs4QFIfADSjzwVFaBhg+pnWMtZd+TNZTYKDsCqknq52myz1MAwKfBTykp76AdldsfgzTEhdoPUuK+YmclgJESPcLxAuatN5PAwGVEXE3scmM4sFQwl2sabasL+M0SQ1avfrkvDexZ7tnsZitD1Gwmb+AKzxSYQTG5bQeDwht3+uI6za5iX4JM+Zslf2/W45c6eHMafV84LDU8kBMUAe80CgYEA6qd352ANVXNn3Jo5l8eOiellJwQ3xOE3ESJ12/etEyZ5ZfRecHBHAP7whDQSYbW6xC4X969g/jrtHZOwZ4B8ETrquTxbZs/na04gTyvgf4MIdqvad9rOcmRo2LxY/LgNSxhoDHn76crk+oSkvhfuCCTrj7awuqRNLbLg/McCfZsCgYEA5u26HK5YczJvARHcY2LrEI1Xt6J8SfBUlm6Wcov/LtHQICPW3ZvtpnP2xG4nHZ7c4XM1sAcFAK0lxwhf9xt8o0mznlYN5LrTIFg2sHoQ29m2LuzQUKUN1IP6B/MwTxdcXSrDPfpguBMrI8LnhySyMlNTlQ5Gidszbpy5s3ZKdeMCgYEA5MvniK3KMoB1S88AyvJkFCqDW3isXAZwp/9exd0IX7zK79NG5gFD6j+qCm2vYqBMfA6phfL18s1H9+fSQAkyb6Zvya+FO4kKD0G9FRUmL453CSblvKmXVEh9Rp7XcYqQQ6GHimrCayJPA8mjzEoO6Nf+60DprwKW0jExWKc+0XMCgYAMsTAzd1mhKzpyopqsU7l7tWkGzMVsAuuDQRy/uvYRirKXsaCTmYhcR69eaHd550tYkM54mEosGVgkMk+j8zzMdLaMk0o8MhB8jJyk4nCexL6Aob9pT0kNTxuk6DbVggEvlbQZafN66oLKemHjSZ8JqQ7E+H3kAco34CFMvcml5QKBgEEU8iXkBEdFYq+l8mtihVUT5xG0vGpvawQGH18S1AGmSH5++fsHm3rTk26hsZDB0qM+nLWvJ/ioXJRQhhKpHf6y6x8mWdckC8S+2vw3jQvM3TrwMBwusMb+Cz1xEqHp2hx4ib27KGJAVY6D5iDqY2SwjrGQkqdjoz0nUUF3HdwX";
        String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnaVEs2U8XFCglT1cyN/h8sq5hfu38FdyARvVNHY9sOdMqvdIuEymRYvFSkje5IROzs2ul6AzPj3CmrVNdfFUf7dPMdEAlVfuxz3MSd64ReQIz07BqBf5jfIm7aEWniMv/Dv5PMrQghrlGqYGrn/KXddcOKhLQ2MSWl2jwLUfhcSJiyiCXfKmZ39b4d9yfpzFjzuXqe8py0qz08fx97/4Ni1+Hl5C5T/uZZsFwCEFfD3rNveWL9DWo/6hnRQn/ZOUO6djc7qp2oMkQu9etpyI/moO123CYzJ7Ggu6N959DPD67+NyqFtKEsSdwDfgOvf0WOPMA4Q5Lb3aM5foZoLuywIDAQAB";
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(SANDBOX_URL);
        alipayConfig.setAppId("9021000151646079");
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        return alipayConfig;
    }

}