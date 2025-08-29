package com.ds.aether.server.controller;

import javax.annotation.Resource;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ds.aether.core.model.Result;
import com.ds.aether.server.model.User;
import com.ds.aether.server.model.dto.LoginRequest;
import com.ds.aether.server.model.dto.RegisterRequest;
import com.ds.aether.server.service.UserService;
import com.ds.aether.server.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
@Slf4j
@SaIgnore
@RestController
@RequestMapping("auth")
public class AuthController {

    // 密码加密盐值
    private static final String PASSWORD_SALT = "aether_salt_2025";

    @Resource
    private RSAUtil rsaUtil;

    @Resource
    private UserService userService;

    /**
     * 获取RSA公钥
     */
    @GetMapping("/public-key")
    public Result getPublicKey() {
        return Result.okData(rsaUtil.getPublicKeyString());
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        // 参数校验
        if (StrUtil.hasBlank(username, password)) {
            return Result.fail("用户名或密码不能为空!");
        }

        // 用户名长度校验
        if (username.length() < 3) {
            return Result.fail("用户名长度不能少于3位!");
        }

        // 密码长度校验
        if (password.length() < 6) {
            return Result.fail("密码长度不能少于6位!");
        }

        // 检查用户是否已存在
        if (userService.userExists(username)) {
            return Result.fail("用户名已存在!");
        }

        try {
            // 解密密码
            String decryptedPassword = rsaUtil.decrypt(password);
            // 加密存储密码 (使用SHA256 + salt)
            String encryptedPassword = DigestUtil.sha256Hex(PASSWORD_SALT + decryptedPassword);
            // 存储用户信息
            return userService.save(username, encryptedPassword);
        } catch (Exception e) {
            log.error("注册异常：", e);
        }
        return Result.fail("注册失败");
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
        String decryptedPassword = rsaUtil.decrypt(password);
        // 加密存储密码 (使用SHA256 + salt)
        String encryptedPassword = DigestUtil.sha256Hex(PASSWORD_SALT + decryptedPassword);
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        User loginUser = userService.login(username, encryptedPassword);
        if (loginUser != null) {
            StpUtil.login(loginUser.getId());
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
    @RequestMapping("check")
    public String info() {
        return "权限校验";
    }

}
