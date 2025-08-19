package com.ds.aether.server.util;

import static com.ds.aether.server.util.AMapConfig.*;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.CollectionUtils;

/**
 * @author ds
 * @date 2025/8/18
 * @description
 */
public class WeatherUtil {

    public static String getLiveWeather(String address) {
        String geocodeResultStr = HttpUtil.get(BASE_URL + "geocode/geo?key=" + KEY + "&address=" + address);
        String adcode = "";
        if (StrUtil.isNotBlank(geocodeResultStr)) {
            JSONObject geocodeResult = JSON.parseObject(geocodeResultStr);
            String infocode = geocodeResult.getString(INFOCODE);
            if (SUCCESS_CODE.equals(infocode)) {
                JSONArray geocodeArray = geocodeResult.getJSONArray("geocodes");
                if (!CollectionUtils.isEmpty(geocodeArray)) {
                    adcode = geocodeArray.getJSONObject(0).getString("adcode");
                }
            }
        }

        if (StrUtil.isNotBlank(adcode)) {
            String weatherInfoResultStr = HttpUtil.get(BASE_URL + "weather/weatherInfo?key=" + KEY + "&city=" + adcode + "&extensions=base");
            if (StrUtil.isNotBlank(weatherInfoResultStr)) {
                JSONObject weatherInfoResult = JSON.parseObject(weatherInfoResultStr);
                String infocode = weatherInfoResult.getString(INFOCODE);
                if (SUCCESS_CODE.equals(infocode)) {
                    JSONArray weatherInfoArray = weatherInfoResult.getJSONArray("lives");
                    if (!CollectionUtils.isEmpty(weatherInfoArray)) {
                        JSONObject weatherInfo = weatherInfoArray.getJSONObject(0);
                        return address + "：" + weatherInfo.getString("weather") + "，" + weatherInfo.getString("temperature") + "℃";
                    }
                }
            }
        }

        return "";
    }

}
