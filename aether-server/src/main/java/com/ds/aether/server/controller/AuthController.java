package com.ds.aether.server.controller;

import javax.annotation.Resource;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.ds.aether.core.model.Result;
import com.ds.aether.server.model.dto.LoginRequest;
import com.ds.aether.server.util.RSAUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private RSAUtil rsaUtil;

    /**
     * 获取RSA公钥
     */
    @GetMapping("/public-key")
    public Result getPublicKey() {
        return Result.okData(rsaUtil.getPublicKeyString());
    }

    // 测试登录，浏览器访问： http://localhost:23843/api/auth/login?username=zhang&password=123456
    @RequestMapping("login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (StrUtil.hasBlank(username, password)) {
            return Result.fail("用户名或密码不能为空!");
        }

        // 解密密码
        password = rsaUtil.decrypt(password);

        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if ("admin".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return Result.ok("登录成功!");
        }

        return Result.fail("用户名或密码错误!");
    }

    // 查询登录状态，浏览器访问： http://localhost:23843/api/auth/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        String loginIdAsString = StpUtil.getLoginIdAsString();
        System.out.println("loginIdAsString = " + loginIdAsString);
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 测试注销  ---- http://localhost:23843/api/auth/logout
    @RequestMapping("logout")
    public Result logout() {
        StpUtil.logout();
        return Result.ok("注销成功");
    }

    // 测试登录拦截器  ---- http://localhost:23843/api/auth/check
    @SaCheckLogin
    @RequestMapping("check")
    public String info() {
        return "权限校验";
    }

}
