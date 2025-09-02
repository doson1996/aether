package com.ds.aether.server.util;

import javax.annotation.Resource;

import cn.dev33.satoken.stp.StpUtil;
import com.ds.aether.server.model.User;
import com.ds.aether.server.repo.MongoRepo;
import com.ds.aether.server.service.UserService;
import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/9/2
 * @description
 */
@Component
public class LoginUtil {

    @Resource
    private UserService userService;

    public String getUsername() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUsername();
    }

    public User getLoginUser() {
        if (!isLogin()) {
            return null;
        }

        String userId = getUserId();
        return userService.findById(userId);
    }

    public String getUserId() {
        return StpUtil.getLoginIdAsString();
    }

    public boolean isLogin() {
        return StpUtil.isLogin();
    }

}
