package com.ds.aether.server.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.ds.aether.core.model.Result;
import com.ds.aether.server.constant.PayType;
import com.ds.aether.server.model.dto.PayRequest;
import com.ds.aether.server.model.dto.PayStatusRequest;
import com.ds.aether.server.pay.AliPayUtil;
import com.ds.aether.server.pay.AlipaySandboxTradePagePay;
import com.ds.aether.server.service.OrderService;
import com.ds.aether.server.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Resource
    private AlipaySandboxTradePagePay alipayService;

    @Resource
    private OrderService orderService;

    @Value("${aether.order.timeout:1}")
    private Integer orderTimeout;

    @Override
    public Result payment(PayRequest param) {
        try {
            param.setPayType(PayType.ALIPAY);
            // todo 获取支付信息,根据商品id获取价格
            String productId = param.getProductId();
            // 订单id
            String outTradeNo = orderService.genOrderNo();
            log.info("订单号: {}", outTradeNo);
            String totalAmount = param.getTotalAmount();
            String subject = param.getSubject();
            String body = param.getBody();

            // 订单超时时间, 1小时
            String timeExpire = DateUtil.offsetHour(new Date(), orderTimeout).toString();
            // 调用支付服务生成支付页面
            String form = alipayService.createPayment(outTradeNo, totalAmount, subject, body, timeExpire);
            return Result.ok("支付请求成功", form);
        } catch (Exception e) {
            log.error("支付请求失败: ", e);
        }
        return Result.fail("支付请求失败");
    }

    @Override
    public Result status(PayStatusRequest payStatusRequest) {
        String outTradeNo = payStatusRequest.getOutTradeNo();
        String tradeNo = payStatusRequest.getTradeNo();
        try {
            String status = alipayService.paymentStatus(outTradeNo, tradeNo);
            return Result.okData(status);
        } catch (AlipayApiException e) {
            log.error("支付状态查询异常：", e);
        }

        return Result.fail("支付状态查询失败");
    }

    @Override
    public String returnUrl(HttpServletRequest request, Model model) {
        // 获取支付宝回调参数
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        // 验证签名
        AlipayConfig alipayConfig = alipayService.getSandboxAlipayConfig();
        boolean verifyResult = AliPayUtil.check(params, alipayConfig);

        model.addAttribute("success", verifyResult);
        model.addAttribute("outTradeNo", params.get("out_trade_no"));
        model.addAttribute("totalAmount", params.get("total_amount"));
        model.addAttribute("tradeNo", params.get("trade_no"));
        model.addAttribute("timestamp", params.get("timestamp"));

        return "pay/result";
    }

    @Override
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理支付宝异步通知
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        // 验证签名和处理业务逻辑
        // todo 验证签名并更新订单状态
        AlipayConfig alipayConfig = alipayService.getSandboxAlipayConfig();
        boolean verifyResult = AliPayUtil.check(params, alipayConfig);
        if (verifyResult) {
            log.info("支付成功: {}", params.get("out_trade_no"));
        }
        // 响应支付宝
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // 必须返回success
        out.print("success");
        out.close();
    }

}
