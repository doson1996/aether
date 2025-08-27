package com.ds.aether.server.constant;

/**
 * @author ds
 * @date 2023/4/20
 * @description
 */
public interface MessageConstant {

    /**
     * 限制发送频率存放的value
     */
    String LIMIT_VALUE = "1";

    /**
     * 默认限制发送频率（每分钟限制发送一次）
     */
    Integer LIMIT_VALUE_EXPIRE = 1;

    /**
     * 验证码默认有效期(3分钟)
     */
    Long DEFAULT_EXPIRE = 3L;

}
