package com.ds.aether.server.constant;

/**
 * @author ds
 * @date 2023/4/6
 * @description 邮件模板
 */
public interface MailTemplate {

    /**
     * 验证码
     */
    String VERIFY_CODE = "message/verifyCodeTemplate";

    /**
     * 激活账号
     */
    String ACTIVATE_ACCOUNT = "message/activateTemplate";

}
