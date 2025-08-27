package com.ds.aether.server.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import cn.hutool.core.util.StrUtil;
import com.ds.aether.core.model.Result;
import com.ds.aether.server.constant.DateUnit;
import com.ds.aether.server.constant.MailTemplate;
import com.ds.aether.server.model.dto.SendCaptchaRequest;
import com.ds.aether.server.model.dto.SendMailRequest;
import com.ds.aether.server.model.dto.VerifyCaptchaRequest;
import com.ds.aether.server.service.CaptchaService;
import com.ds.aether.server.service.EmailService;
import com.ds.aether.server.service.LimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author ds
 * @date 2025/8/27
 * @description
 */

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private static final String EMAIL_LIMIT_KEY = "aether:message:limit:";

    private static final String EMAIL_CAPTCHA_KEY = "aether:message:email:captcha:";

    @Resource
    private LimitService limitService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username:doson1996@vip.qq.com}")
    private String from;

    @Override
    public boolean sendHtmlMail(SendMailRequest request) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //创建邮件正文
            Context context = new Context();
            context.setVariables(request.getVariables());
            String emailContent = templateEngine.process(request.getTemplate(), context);

            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送html邮件失败! request: {}, ex: ", request, e);
            return false;
        }

        log.info("发送html邮件成功!");
        return true;
    }

    @Override
    public Result<String> sendCaptcha(SendCaptchaRequest request) {
        request.setExpire(1);
        request.setOperate("购买aether服务");
        request.setSubject("支付验证码");

        // 收件人
        String to = request.getTo();
        // 限制频率key
        String limitKey = limitService.getLimitKey(to, EMAIL_LIMIT_KEY);
        // 判断是否限制发送频率
        if (limitService.getLimit(limitKey)) {
            return Result.fail("邮件发送失败", "请勿频繁请求发送邮箱验证码");
        }

        // 生成验证码
        String captcha = captchaService.generate();
        // 验证码有效期
        Long expire = captchaService.getExpire(request.getExpire());
        Map<String, Object> variables = new HashMap<>(16);
        variables.put("operate", request.getOperate());
        variables.put("expire", expire);
        variables.put("verifyCode", captcha);

        SendMailRequest sendMailRequest = new SendMailRequest();
        // 验证码模版
        sendMailRequest.setTemplate(MailTemplate.VERIFY_CODE);
        // 发送html邮件时模板引擎参数
        sendMailRequest.setVariables(variables);
        // 收件人
        sendMailRequest.setTo(to);
        // 邮件标题
        sendMailRequest.setSubject(request.getSubject());
        boolean sendSuccess = sendHtmlMail(sendMailRequest);
        if (sendSuccess) {
            onSendCaptchaSuccess(request, captcha, limitKey);
            return Result.ok("发送邮件验证码成功!");
        }
        return Result.fail("发送邮件验证码失败!");
    }

    @Override
    public Result<String> verifyCaptcha(VerifyCaptchaRequest request) {
        if (StrUtil.hasBlank(request.getCaptcha(), request.getEmail())) {
            return Result.fail("验证失败", "验证失败");
        }
        String key = captchaService.getCaptchaKey(request.getEmail(), EMAIL_CAPTCHA_KEY);
        boolean verifySuccess = captchaService.verify(request.getCaptcha(), key);
        if (verifySuccess) {
            return Result.ok("验证成功", "验证成功");
        }
        return Result.fail("验证失败", "验证失败");
    }

    /**
     * 发送验证码成功后
     *  todo 发送成功，异步处理
     *
     * @param request
     */
    private void onSendCaptchaSuccess(SendCaptchaRequest request, String captcha, String limitKey) {
        String key = captchaService.getCaptchaKey(request.getTo(), EMAIL_CAPTCHA_KEY);
        long keyExpire = request.getExpire() * DateUnit.MINUTE.getSecond();
        // 存放邮箱和验证码到redis并设置过期时间
        captchaService.save(key, captcha, keyExpire);
        // 存放邮箱到发送频率限制key中（发送成功后，一分钟内不可再次发送）
        limitService.setLimit(limitKey);
    }
}
