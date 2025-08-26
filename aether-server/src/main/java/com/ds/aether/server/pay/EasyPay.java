package com.ds.aether.server.pay;

import java.util.UUID;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.page.Client;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;

/**
 * @author ds
 * @date 2025/8/26
 * @description
 */
public class EasyPay {

    public static void main(String[] args) throws Exception {
        Factory.setOptions(getAlipayConfig());
        Client page = Factory.Payment.Page();
        AlipayTradePagePayResponse response = page.pay("测试商品", UUID.randomUUID().toString(), "88.88", "127.0.0.1");
        System.out.println("response = " + response);
    }

    private static Config getAlipayConfig() {
        Config config = new Config();
        config.protocol = "http";
        config.gatewayHost = "openapi.alipaydev.com";
        config.appId = "9021000151646079";
        config.signType = "RSA2";
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnaVEs2U8XFCglT1cyN/h8sq5hfu38FdyARvVNHY9sOdMqvdIuEymRYvFSkje5IROzs2ul6AzPj3CmrVNdfFUf7dPMdEAlVfuxz3MSd64ReQIz07BqBf5jfIm7aEWniMv/Dv5PMrQghrlGqYGrn/KXddcOKhLQ2MSWl2jwLUfhcSJiyiCXfKmZ39b4d9yfpzFjzuXqe8py0qz08fx97/4Ni1+Hl5C5T/uZZsFwCEFfD3rNveWL9DWo/6hnRQn/ZOUO6djc7qp2oMkQu9etpyI/moO123CYzJ7Ggu6N959DPD67+NyqFtKEsSdwDfgOvf0WOPMA4Q5Lb3aM5foZoLuywIDAQAB";
        config.merchantPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDTrF1b2L4G3ezWk+SmprCAFYMpItsYmkN9riUiumzAWATlekO6oP88FctCm8b4l93lzUsX5d0kGLDSmqAYy4dz0TBXfmy6bEEZSyZ80AMifz7yFNMTaIMB3rtKhzkflfaRLCBKfVcWugMsRyq7WuB2rQEFd9Jql3Agld+NBno4nvRMy/7MVXS5YR5W08VPzLl8Wzgqy2yLxZCEAH8mYqEfS8IDIXglpG93qcgjWVwXoTiis1MqZHlllpqUbHgaJ5PpRCP32Vw4Mx/t8VaGVpUfuw2ckb6eCx3a8yZHMXvEL/XuMe0zjZ48zrT5nJiSoWTYBr3gZRKSqes0ihdbazdxAgMBAAECggEBAMILfh5HyJRk98wSEgeQQbd5gR7B3FZmfL6HWXXHrxB44Cw6dEJveuvrEsXCJpoYJoyXMWL8D0ka9WZr554zXr4WpBlhRW109gyo7uR2kJkcS2kDHCjro0WgmNOOuRgykrGs4QFIfADSjzwVFaBhg+pnWMtZd+TNZTYKDsCqknq52myz1MAwKfBTykp76AdldsfgzTEhdoPUuK+YmclgJESPcLxAuatN5PAwGVEXE3scmM4sFQwl2sabasL+M0SQ1avfrkvDexZ7tnsZitD1Gwmb+AKzxSYQTG5bQeDwht3+uI6za5iX4JM+Zslf2/W45c6eHMafV84LDU8kBMUAe80CgYEA6qd352ANVXNn3Jo5l8eOiellJwQ3xOE3ESJ12/etEyZ5ZfRecHBHAP7whDQSYbW6xC4X969g/jrtHZOwZ4B8ETrquTxbZs/na04gTyvgf4MIdqvad9rOcmRo2LxY/LgNSxhoDHn76crk+oSkvhfuCCTrj7awuqRNLbLg/McCfZsCgYEA5u26HK5YczJvARHcY2LrEI1Xt6J8SfBUlm6Wcov/LtHQICPW3ZvtpnP2xG4nHZ7c4XM1sAcFAK0lxwhf9xt8o0mznlYN5LrTIFg2sHoQ29m2LuzQUKUN1IP6B/MwTxdcXSrDPfpguBMrI8LnhySyMlNTlQ5Gidszbpy5s3ZKdeMCgYEA5MvniK3KMoB1S88AyvJkFCqDW3isXAZwp/9exd0IX7zK79NG5gFD6j+qCm2vYqBMfA6phfL18s1H9+fSQAkyb6Zvya+FO4kKD0G9FRUmL453CSblvKmXVEh9Rp7XcYqQQ6GHimrCayJPA8mjzEoO6Nf+60DprwKW0jExWKc+0XMCgYAMsTAzd1mhKzpyopqsU7l7tWkGzMVsAuuDQRy/uvYRirKXsaCTmYhcR69eaHd550tYkM54mEosGVgkMk+j8zzMdLaMk0o8MhB8jJyk4nCexL6Aob9pT0kNTxuk6DbVggEvlbQZafN66oLKemHjSZ8JqQ7E+H3kAco34CFMvcml5QKBgEEU8iXkBEdFYq+l8mtihVUT5xG0vGpvawQGH18S1AGmSH5++fsHm3rTk26hsZDB0qM+nLWvJ/ioXJRQhhKpHf6y6x8mWdckC8S+2vw3jQvM3TrwMBwusMb+Cz1xEqHp2hx4ib27KGJAVY6D5iDqY2SwjrGQkqdjoz0nUUF3HdwX";
        return config;
    }
}
