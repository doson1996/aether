package com.ds.aether.server.service;

/**
 * @author ds
 * @date 2023/4/22
 * @description 验证码
 */
public interface CaptchaService {

    /**
     * redis存验证码 key (key 前缀:key value 验证码)
     *
     * @param key
     * @param prefix
     * @return
     */
    String getCaptchaKey(String key, String prefix);

    /**
     * 生成验证码
     *
     * @return
     */
    String generate();

    /**
     * 获取验证码
     *
     * @param key
     * @return
     */
    String fetch(String key);

    /**
     * 存放验证码
     *
     * @param key
     * @param captcha
     */
    void save(String key, String captcha);

    /**
     * 存放验证码
     *
     * @param key
     * @param captcha
     */
    void save(String key, String captcha, Long seconds);

    /**
     * 验证验证码（只能成功验证一次）
     *
     * @param captcha
     * @param key
     * @return
     */
    boolean verify(String captcha, String key);

    /**
     * 验证验证码
     *
     * @param captcha
     * @param key
     * @param repeat  是否在有效期内可重复成功验证
     * @return
     */
    boolean verify(String captcha, String key, boolean repeat);

    /**
     * 获取验证码有效期,没设置则返回默认有效期(3分钟)
     * @param expire
     * @return
     */
    Long getExpire(Integer expire);

}
