package com.ds.aether.server.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayConfig;
import com.ds.aether.server.pay.AliPayUtil;
import com.ds.aether.server.pay.AlipaySandboxTradePagePay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 支付宝支付控制器
 */
@Slf4j
@Controller
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private AlipaySandboxTradePagePay alipayService;



    /**
     * 获取商品信息接口
     */
    @GetMapping("/product/{productId}/{userId}")
    @ResponseBody
    public Map<String, Object> getProductInfo(@PathVariable String productId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 这里应该从数据库或其他服务获取商品信息
            // 示例数据，实际应该根据productId查询真实数据
            Map<String, Object> product = new HashMap<>();

            String orderNo = genOrderNo();
            log.info("orderNo = " + orderNo + ", userId = " + userId);
            if ("1".equals(productId)) {
                product.put("id", productId);
                product.put("name", "Aether基础版服务");
                product.put("description", "Aether任务调度系统基础版服务费用");
                product.put("price", "99.00");
                product.put("orderNo", orderNo);
            } else if ("2".equals(productId)) {
                product.put("id", productId);
                product.put("name", "Aether专业版服务");
                product.put("description", "Aether任务调度系统专业版服务费用");
                product.put("price", "299.00");
                product.put("orderNo", orderNo);
            } else if ("3".equals(productId)) {
                product.put("id", productId);
                product.put("name", "Aether企业版服务");
                product.put("description", "Aether任务调度系统企业版服务费用");
                product.put("price", "999.00");
                product.put("orderNo", orderNo);
            } else {
                // 默认商品信息
                product.put("id", productId);
                product.put("name", "Aether基础版服务");
                product.put("description", "Aether任务调度系统基础版服务费用");
                product.put("price", "99.00");
                product.put("orderNo", orderNo);
            }

            result.put("success", true);
            result.put("data", product);
            result.put("message", "获取商品信息成功");
        } catch (Exception e) {
            log.error("获取商品信息失败", e);
            result.put("success", false);
            result.put("message", "获取商品信息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 生成订单号
     *
     * @return
     */
    private String genOrderNo() {
        return "ORDER" + System.currentTimeMillis();
    }

    /**
     * 处理支付请求
     */
    @PostMapping("/pay")
    @ResponseBody
    public Map<String, Object> handlePayment(@RequestBody Map<String, String> paymentInfo) {
        Map<String, Object> result = new HashMap<>();
        try {
            String outTradeNo = paymentInfo.get("outTradeNo");
            String totalAmount = paymentInfo.get("totalAmount");
            String subject = paymentInfo.get("subject");
            String body = paymentInfo.get("body");

            // 调用支付服务生成支付页面
            String form = alipayService.createPayment(outTradeNo, totalAmount, subject, body);

            result.put("success", true);
            result.put("data", form);
            result.put("message", "支付请求成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "支付请求失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 支付宝同步回调页面
     */
    @GetMapping("/return")
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

        // 验证签名（简化处理，实际应验证签名）
        AlipayConfig alipayConfig = alipayService.getSandboxAlipayConfig();
        boolean verifyResult = AliPayUtil.check(params, alipayConfig);
        // boolean success = "TRADE_SUCCESS".equals(params.get("trade_status")) || "TRADE_FINISHED".equals(params.get("trade_status"));

        model.addAttribute("success", verifyResult);
        model.addAttribute("outTradeNo", params.get("out_trade_no"));
        model.addAttribute("totalAmount", params.get("total_amount"));
        model.addAttribute("tradeNo", params.get("trade_no"));
        model.addAttribute("timestamp", params.get("timestamp"));

        return "alipay/result";
    }

    /**
     * 支付宝异步通知接口
     */
    @PostMapping("/notify")
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
        // 这里应该验证签名并更新订单状态

        // 响应支付宝
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("success"); // 必须返回success
        out.close();
    }
}
