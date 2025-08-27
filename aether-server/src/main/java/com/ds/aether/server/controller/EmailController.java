package com.ds.aether.server.controller;

import javax.annotation.Resource;

import com.ds.aether.core.model.Result;
import com.ds.aether.server.model.dto.SendCaptchaRequest;
import com.ds.aether.server.model.dto.VerifyCaptchaRequest;
import com.ds.aether.server.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ds
 * @date 2025/8/27
 * @description
 */
@Slf4j
@RestController
@RequestMapping("email")
public class EmailController {

    @Resource
    private EmailService emailService;

    /**
     * 发送验证码
     *
     * @param request
     * @return
     */
    @PostMapping("send-captcha")
    public Result<String> sendCaptcha(@RequestBody SendCaptchaRequest request) {
        return emailService.sendCaptcha(request);
    }

    /**
     * 验证短信验证码
     *
     * @param request
     * @return
     */
    @PostMapping("verify-captcha")
    public Result<String> verifyCaptcha(@RequestBody VerifyCaptchaRequest request) {
        return emailService.verifyCaptcha(request);
    }

}
