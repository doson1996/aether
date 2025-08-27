package com.ds.aether.server.service;

/**
 * @author ds
 * @date 2023/4/22
 * @description
 */
public interface LimitService {

    /**
     * 获取频率限制key
     * @param key
     * @param prefix
     * @return
     */
    String getLimitKey(String key, String prefix);

    /**
     * 是否限流
     * @return
     */
    boolean getLimit(String key);

    /**
     * 存放key到发送频率限制key中（发送成功后，默认一分钟内不可再次发送）
     * 设置限流
     * @param key
     * @return
     */
    void setLimit(String key);

    /**
     * 设置限流
     * 存放key到发送频率限制key中（发送成功后，seconds秒内不可再次发送）
     * @param key
     * @param seconds
     * @return
     */
    void setLimit(String key, Long seconds);

}
