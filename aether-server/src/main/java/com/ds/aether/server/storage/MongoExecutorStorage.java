package com.ds.aether.server.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson2.JSON;
import com.ds.aether.core.constant.ExecutorStatus;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.ds.aether.server.repo.MongoRepo;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

/**
 * @author ds
 * @date 2025/5/14
 * @description
 */
@Repository
public class MongoExecutorStorage implements ExecutorStorage {

    public static final String TABLE = "executor";

    @Resource
    private MongoRepo mongoRepo;

    @Override
    public Map<String, ExecutorInfo> findAvailableExecutors() {
        return mongoRepo.list(TABLE, null, null, null, null, true).stream()
                .map(document -> JSON.parseObject(document.toJson(), ExecutorInfo.class))
                .filter(entry -> !ExecutorStatus.OFFLINE.equals(entry.getStatus()))
                .collect(Collectors.toMap(ExecutorInfo::getName, executorInfo -> executorInfo));
    }

    @Override
    public Map<String, ExecutorInfo> findAll() {
        Map<String, ExecutorInfo> result = new HashMap<>();
        List<Document> list = mongoRepo.list(TABLE, null, null, null, null, true);
        for (Document document : list) {
            ExecutorInfo executorInfo = JSON.parseObject(document.toJson(), ExecutorInfo.class);
            Long lastHeartbeat = document.getLong("lastHeartbeat");
            executorInfo.setLastHeartbeat(lastHeartbeat);
            result.put(executorInfo.getName(), executorInfo);
        }
        return result;
    }

    @Override
    public ExecutorInfo find(String name) {
        Bson condition = Filters.eq("name", name);
        Document one = mongoRepo.findOne(TABLE, condition);
        return one == null ? null : JSON.parseObject(one.toJson(), ExecutorInfo.class);
    }

    @Override
    public boolean exist(String name) {
        Bson condition = Filters.eq("name", name);
        return mongoRepo.count(TABLE, condition) > 0;
    }

    @Override
    public boolean add(ExecutorInfo executorInfo) {
        // 参数校验
        if (executorInfo == null || executorInfo.getName() == null) {
            return false;
        }

        Bson condition = Filters.eq("host", executorInfo.getHost());
        mongoRepo.saveOrUpdate(TABLE, condition, Document.parse(JSON.toJSONString(executorInfo)));
        return true;
    }

    @Override
    public boolean remove(String name) {
        DeleteResult result = mongoRepo.deleteMany(TABLE, new Document("name", name));
        return result.wasAcknowledged();
    }

    @Override
    public void removeAll() {
        mongoRepo.deleteMany(TABLE, new Document());
    }

    @Override
    public void update(ExecutorInfo executorInfo) {
        mongoRepo.updateMany(TABLE, new Document("name", executorInfo.getName()), new Document("$set", Document.parse(JSON.toJSONString(executorInfo))));
    }

}
