package com.ds.aether.server.service.impl;

import javax.annotation.Resource;

import com.ds.aether.server.constant.DateUnit;
import com.ds.aether.server.constant.MessageConstant;
import com.ds.aether.server.service.LimitService;
import com.ds.aether.server.util.RedisUtil;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2023/4/22
 * @description
 */
@Service
public class LimitServiceImpl implements LimitService, MessageConstant {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String getLimitKey(String key, String prefix) {
        return prefix + key;
    }

    @Override
    public boolean getLimit(String key) {
        // 判断是否限制发送频率
        String limitValue = redisUtil.get(key);
        return LIMIT_VALUE.equals(limitValue);
    }

    @Override
    public void setLimit(String key) {
        setLimit(key, LIMIT_VALUE_EXPIRE * DateUnit.MINUTE.getSecond());
    }

    @Override
    public void setLimit(String key, Long seconds) {
        redisUtil.set(key, LIMIT_VALUE, seconds);
    }

}
