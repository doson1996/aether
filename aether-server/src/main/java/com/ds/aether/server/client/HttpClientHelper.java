package com.ds.aether.server.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.constant.ClientConstant;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.server.ExecutorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ds
 * @date 2025/4/14
 * @description
 */
@Slf4j
@Component
public class HttpClientHelper implements ClientHelper {

    @Override
    public boolean sendTaskToExecutor(ExecutorInfo executor, ExecJobParam param) {
        log.info("发送任务【{}】到执行器【{}】", param.getJobName(), executor.getName());
        try {
            String url = executor.getHost() + executor.getContextPath() + ClientConstant.CLIENT_EXEC_JOB_FULL_PATH;
            String resultStr = HttpUtil.post(url, JSONObject.toJSONString(param));
            if (StrUtil.isNotBlank(resultStr)) {
                JSONObject resultJson = JSONObject.parseObject(resultStr);
                if (resultJson.getBoolean("success")) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("发送任务【{}】到执行器【{}】异常：", param.getJobName(), executor.getName(), e);
        }
        return false;
    }

}
