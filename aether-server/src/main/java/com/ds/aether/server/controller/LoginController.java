package com.ds.aether.server.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.ds.aether.core.model.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
@RestController
@RequestMapping("login")
public class LoginController {

    // 测试登录，浏览器访问： http://localhost:23843/api/login/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public String doLogin(String username, String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if ("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return "登录成功";
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:23843/api/login/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        String loginIdAsString = StpUtil.getLoginIdAsString();
        System.out.println("loginIdAsString = " + loginIdAsString);
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 测试注销  ---- http://localhost:23843/api/login/logout
    @RequestMapping("logout")
    public Result logout() {
        StpUtil.logout();
        return Result.ok("注销成功");
    }

    // 测试登录拦截器  ---- http://localhost:23843/api/login/check
    @SaCheckLogin
    @RequestMapping("check")
    public String info() {
        return "权限校验";
    }

}
