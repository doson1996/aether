package com.ds.aether.server.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.ds.aether.core.model.Result;
import com.ds.aether.server.constant.UserStatus;
import com.ds.aether.server.model.User;
import com.ds.aether.server.repo.MongoRepo;
import com.ds.aether.server.service.UserService;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 * @author ds
 * @date 2025/8/29
 * @description
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    public static final String USER_TABLE = "user";

    @Resource
    private MongoRepo mongoRepo;

    @Override
    public User login(String username, String password) {
        Document document = mongoRepo.findOne(USER_TABLE, Filters.and(Filters.eq("username", username), Filters.eq("password", password)));
        if (document == null) {
            return null;
        }
        return JSON.parseObject(document.toJson(), User.class);
    }

    @Override
    public Boolean userExists(String username) {
        log.info("username: {}", username);
        return mongoRepo.count("user", new Document("username", username)) > 0;
    }

    @Override
    public Result save(String username, String encryptedPassword) {
        if (userExists(username)) {
            return Result.fail("用户名" + username + "已存在");
        }
        User user = new User();
        user.setId(IdUtil.getSnowflakeNextIdStr());
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setStatus(UserStatus.NORMAL);
        mongoRepo.insert(USER_TABLE, Document.parse(JSON.toJSONString(user)));
        return Result.ok("注册成功!");
    }

    @Override
    public User findById(String id) {
        Document document = mongoRepo.findOne(USER_TABLE, Filters.and(Filters.eq("id", id)));
        if (document == null) {
            return null;
        }
        return JSON.parseObject(document.toJson(), User.class);
    }
    
}
