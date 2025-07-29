package com.ds.aether.server.repo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/7/29
 * @description
 */
@Component
public class MongoRepo {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 插入单个文档
     *
     * @param table    集合名称
     * @param document 要插入的文档
     * @return 插入的文档
     */
    public Document insert(String table, Document document) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        collection.insertOne(document);
        return document;
    }

    /**
     * 批量插入文档
     *
     * @param table     集合名称
     * @param documents 要插入的文档列表
     * @return 插入的文档列表
     */
    public List<Document> insertMany(String table, List<Document> documents) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        collection.insertMany(documents);
        return documents;
    }

    /**
     * 根据条件查找单个文档
     *
     * @param table     集合名称
     * @param condition 查询条件
     * @return 符合条件的第一个文档
     */
    public Document findOne(String table, Bson condition) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        return collection.find(condition).first();
    }

    /**
     * 根据条件查找文档列表
     *
     * @param table      集合名称
     * @param condition  查询条件
     * @param sort       排序条件
     * @param projection 投影条件
     * @param limit      限制数量
     * @param removeId   是否排除_id字段
     * @return 文档列表
     */
    public List<Document> list(String table, Bson condition, Bson sort, Bson projection, Integer limit, Boolean removeId) {
        List<Document> result = new ArrayList<>();
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);


        // 构建查询
        com.mongodb.client.FindIterable<Document> findIterable;
        if (condition != null) {
            findIterable = collection.find(condition);
        } else {
            findIterable = collection.find();
        }

        // 应用排序
        if (sort != null) {
            findIterable.sort(sort);
        }

        // 应用限制
        if (limit != null && limit > 0) {
            findIterable.limit(limit);
        }

        // 应用投影
        Bson finalProjection = projection;
        if (removeId != null && removeId) {
            if (projection != null) {
                finalProjection = Projections.fields(projection, Projections.excludeId());
            } else {
                finalProjection = Projections.excludeId();
            }
        }

        if (finalProjection != null) {
            findIterable.projection(finalProjection);
        }

        // 执行查询并收集结果
        for (Document document : findIterable) {
            result.add(document);
        }

        return result;
    }

    /**
     * 更新单个文档
     *
     * @param table     集合名称
     * @param condition 查询条件
     * @param update    更新内容
     * @return 更新结果
     */
    public UpdateResult updateOne(String table, Bson condition, Bson update) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        return collection.updateOne(condition, update);
    }

    /**
     * 更新多个文档
     *
     * @param table     集合名称
     * @param condition 查询条件
     * @param update    更新内容
     * @return 更新结果
     */
    public UpdateResult updateMany(String table, Bson condition, Bson update) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        return collection.updateMany(condition, update);
    }

    /**
     * 删除单个文档
     *
     * @param table     集合名称
     * @param condition 删除条件
     * @return 删除结果
     */
    public DeleteResult deleteOne(String table, Bson condition) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        return collection.deleteOne(condition);
    }

    /**
     * 删除多个文档
     *
     * @param table     集合名称
     * @param condition 删除条件
     * @return 删除结果
     */
    public DeleteResult deleteMany(String table, Bson condition) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        return collection.deleteMany(condition);
    }

    /**
     * 统计符合条件的文档数量
     *
     * @param table     集合名称
     * @param condition 查询条件
     * @return 文档数量
     */
    public long count(String table, Bson condition) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        return collection.countDocuments(condition);
    }

}
