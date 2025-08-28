package com.ds.aether.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.ds.aether.server.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public String genOrderNo() {
        // 订单号 雪花算法
        return IdUtil.getSnowflakeNextIdStr();
    }

}
