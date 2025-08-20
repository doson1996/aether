package com.ds.aether.server.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.ds.aether.server.entity.AccessLogEntity;
import com.ds.aether.server.repo.MongoRepo;
import com.ds.aether.server.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2025/8/20
 * @description
 */
@Slf4j
@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Resource
    private MongoRepo mongoRepo;

    // 使用线程池异步处理埋点数据
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void logAccess(String className, String methodName, String module, String operation, String params, String result, long duration, boolean success, String errorMsg) {
        // 异步处理埋点数据
        executorService.submit(() -> {
            try {
                AccessLogEntity logEntity = new AccessLogEntity();
                logEntity.setClassName(className);
                logEntity.setMethodName(methodName);
                logEntity.setModule(module);
                logEntity.setOperation(operation);
                logEntity.setParams(params);
                logEntity.setResult(result);
                logEntity.setDuration(duration);
                logEntity.setSuccess(success);
                logEntity.setErrorMsg(errorMsg);
                logEntity.setAccessTime(DateUtil.now());
                logEntity.setUserId(getCurrentUserId());
                logEntity.setUserIp(getClientIp());

                // 保存到数据库或发送到消息队列
                saveAccessLog(logEntity);

                // 实时统计
                updateStatistics(logEntity);

            } catch (Exception e) {
                log.error("埋点统计异常", e);
            }
        });
    }

    private void updateStatistics(AccessLogEntity logEntity) {

    }

    /**
     * 保存到数据库
     *
     * @param logEntity
     */
    private void saveAccessLog(AccessLogEntity logEntity) {
        mongoRepo.insert("access_log", Document.parse(JSON.toJSONString(logEntity)));
    }

    private String getCurrentUserId() {
        return "1";
    }

    private String getClientIp() {
        return "127.0.0.1";
    }

}
