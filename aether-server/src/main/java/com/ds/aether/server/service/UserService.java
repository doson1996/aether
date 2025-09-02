package com.ds.aether.server.service;

import com.ds.aether.core.model.Result;
import com.ds.aether.server.model.User;

/**
 * @author ds
 * @date 2025/8/29
 * @description
 */
public interface UserService {

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 判断用户是否存在
     *
     * @param username
     * @return
     */
    Boolean userExists(String username);

    /**
     * 保存用户
     *
     * @param username
     * @param encryptedPassword
     */
    Result save(String username, String encryptedPassword);

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    User findById(String id);

}
