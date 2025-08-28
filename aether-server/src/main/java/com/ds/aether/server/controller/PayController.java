package com.ds.aether.server.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ds.aether.core.model.Result;
import com.ds.aether.server.model.dto.PayRequest;
import com.ds.aether.server.model.dto.PayStatusRequest;
import com.ds.aether.server.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 支付宝支付控制器
 * todo 状态机控制支付流程
 */
@Slf4j
@Controller
@RequestMapping("/alipay")
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping("/status")
    @ResponseBody
    public Result paymentStatus(@RequestBody PayStatusRequest payStatusRequest) {
        return payService.status(payStatusRequest);
    }

    /**
     * 处理支付请求
     */
    @PostMapping("/pay")
    @ResponseBody
    public Result handlePayment(@RequestBody PayRequest payRequest) {
        try {
            return payService.payment(payRequest);
        } catch (Exception e) {
            log.error("支付请求失败: ", e);
        }
        return Result.fail("支付请求失败");
    }

    /**
     * 支付宝同步回调页面
     */
    @GetMapping("/return")
    public String returnUrl(HttpServletRequest request, Model model) {
        return payService.returnUrl(request, model);
    }

    /**
     * 支付宝异步通知接口
     */
    @PostMapping("/notify")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        payService.notifyUrl(request, response);
    }

}
