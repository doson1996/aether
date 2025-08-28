package com.ds.aether.server.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author ds
 * @date 2025/8/28
 * @description
 */
public class EmailUtil {

    // 邮箱格式验证方法
    public static boolean isValidEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

}
