package com.ds.aether.server.service;

import com.ds.aether.core.model.Result;
import com.ds.aether.server.model.dto.SendCaptchaRequest;
import com.ds.aether.server.model.dto.SendMailRequest;
import com.ds.aether.server.model.dto.VerifyCaptchaRequest;

/**
 *
 * @author ds
 * @date 2025/8/27
 * @description
 */public interface EmailService {

    /**
     * 发送html邮件
     *
     * @param request
     * @return
     */
    boolean sendHtmlMail(SendMailRequest request);

    /**
     * 发送验证码
     * @param request
     * @return
     */
    Result<String> sendCaptcha(SendCaptchaRequest request);

    /**
     * 验证验证码
     * @param request
     * @return
     */
    Result<String> verifyCaptcha(VerifyCaptchaRequest request);

}
