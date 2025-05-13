package com.ds.aether.server.storage;

import com.alibaba.fastjson2.JSON;
import com.ds.aether.core.model.server.ExecutorInfo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ds
 * @date 2025/5/14
 * @description
 */
@Repository
public class MongoExecutorStorage implements ExecutorStorage {

    public static final String TABLE = "executor";

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Map<String, ExecutorInfo> findAvailableExecutors() {
        return new HashMap<>();
    }

    @Override
    public Map<String, ExecutorInfo> findAll() {
        return new HashMap<>();
    }

    @Override
    public ExecutorInfo find(String name) {
        return null;
    }

    @Override
    public boolean exist(String name) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(TABLE);
        Bson condition = Filters.eq("name", name);
        return collection.countDocuments(condition) > 0;
    }

    @Override
    public boolean add(ExecutorInfo executorInfo) {
        // 参数校验
        if (executorInfo == null || executorInfo.getName() == null) {
            return false;
        }

        MongoCollection<Document> collection = mongoTemplate.getCollection(TABLE);

        Document document = Document.parse(JSON.toJSONString(executorInfo));
        collection.insertOne(document);
        return true;
    }

    @Override
    public boolean remove(String name) {
        return false;
    }

    @Override
    public void removeAll() {

    }

    @Override
    public void update(ExecutorInfo executorInfo) {

    }

}
