package com.ds.aether.server.util;

import static com.ds.aether.server.util.AMapConfig.BASE_URL;
import static com.ds.aether.server.util.AMapConfig.KEY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

/**
 * @author ds
 * @date 2025/8/19
 * @description
 */
public class IpUtil {

    private static String publicIp = "";

    public static final List<String> IP_SERVICES = new ArrayList<>(
            Arrays.asList("https://icanhazip.com",
                    "https://api.ipify.org",
                    "https://ident.me")
    );


    public static String getPublicIp() {
        if (StrUtil.isNotBlank(publicIp)) {
            return publicIp;
        }

        for (String service : IP_SERVICES) {
            try {
                String ip = HttpUtil.get(service);
                if (StrUtil.isNotBlank(ip)) {
                    publicIp = ip.trim();
                    IP_SERVICES.remove(service);
                    // 把有返回结果的添加到最前面
                    IP_SERVICES.add(0, service);
                    return publicIp;
                }
            } catch (Exception e) {
                // 继续尝试下一个服务
            }
        }
        return publicIp;
    }

    /**
     * 根据IP获取地理位置信息
     */
    public static JSONObject getLocationByIp(String ip) {
        try {
            String url = BASE_URL + "ip?key=" + KEY + (ip != null ? "&ip=" + ip : "");
            String result = HttpUtil.get(url);
            return JSON.parseObject(result);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return new JSONObject();
    }

    /**
     * 获取当前地理位置信息
     */
    public static JSONObject getCurrentLocation() {
        // 不传ip参数，让高德API自动识别
        return getLocationByIp(null);
    }

    /**
     * 根据指定IP获取地理位置信息
     */
    public static JSONObject getLocationBySpecificIp(String ip) {
        return getLocationByIp(ip);
    }

}
