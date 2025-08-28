package com.ds.aether.server.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ds.aether.core.model.Result;
import com.ds.aether.server.model.dto.PayRequest;
import com.ds.aether.server.model.dto.PayStatusRequest;
import org.springframework.ui.Model;

/**
 * @author ds
 * @date 2025/8/28
 * @description 支付服务
 */
public interface PayService {

    /**
     * 支付接口
     * @param param
     * @return
     */
    Result payment(PayRequest param);

    Result status(PayStatusRequest payStatusRequest);

    /**
     * 支付成功后的回调接口
     * @param request
     * @param model
     * @return
     */
    String returnUrl(HttpServletRequest request, Model model);

    /**
     * 支付成功后的异步通知接口
     * @param request
     * @param response
     */
    void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
