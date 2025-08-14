package com.ds.aether.client.executor;

import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.client.context.AetherContext;
import com.ds.aether.client.job.SpringJobInfo;
import com.ds.aether.core.client.ServerClient;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.client.job.Job;
import com.ds.aether.client.job.JobInfo;
import com.ds.aether.core.model.ExecJobParam;
import com.ds.aether.core.model.Result;
import com.ds.aether.core.model.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ds
 * @date 2025/4/10
 * @description Spring环境执行器
 */
@Slf4j
public class SpringExecutor extends AbstractExecutor implements ApplicationContextAware, SmartInitializingSingleton {

    @Value("${aether.server.host:localhost:8081}")
    private String serverHost;

    @Value("${aether.pause.heartbeat.count:12}")
    private Integer pauseHeartbeatCount;

    @Resource
    private ServerClient serverClient;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    private ApplicationContext context;

    /**
     * 当前暂停发送心跳次数
     */
    private Integer currentPauseHeartbeatCount = 0;

    /**
     * 客户端名称
     */
    private String clientName;

    @Override
    public void registerExecutor() {
        // 获取客户端名称
        getClientName();
        // 客户端ip
        String clientHost = context.getEnvironment().getProperty("aether.client.host", "");
        // 如果没指定ip，自动获取
        if (StrUtil.isBlank(clientHost)) {
            clientHost = NetUtil.getLocalhostStr();
        }
        // 客户端请求端口
        String clientPort = context.getEnvironment().getProperty("server.port", "8080");
        // 客户端请求上下文路径
        String contextPath = context.getEnvironment().getProperty("server.servlet.context-path", "");
        // 应用名
        String appName = context.getEnvironment().getProperty("spring.application.name", "-");

        // 发送注册请求
        serverClient.registerExecutor(appName, clientName, clientHost, clientPort, contextPath);
        log.debug("执行器【{}】已注册", clientName);
    }

    @Override
    public void initJobs() {
        if (context == null) {
            return;
        }

        String[] beanNames = context.getBeanNamesForType(Object.class, false, true);
        for (String beanName : beanNames) {
            Job jobAnnotation = context.findAnnotationOnBean(beanName, Job.class);
            // 如果bean没有@Job注解，跳过处理
            if (jobAnnotation == null) {
                log.debug("bean【{}】没有@Job注解,该bean跳过处理", beanName);
                continue;
            }

            // 任务名
            String jobName = jobAnnotation.name();

            // 如果没配置任务名，已beanName作为任务名
            if (StrUtil.isBlank(jobName)) {
                jobName = beanName;
            }

            // 跳过已存在任务
            boolean existJobInfo = existJobInfo(jobName);
            if (existJobInfo) {
                log.warn("任务【{}】已存在,该bean【{}】跳过处理", jobName, beanName);
            }

            // 任务执行表达式
            String cron = jobAnnotation.cron();

            // 构建任务信息
            JobInfo jobInfo = new SpringJobInfo(jobName, beanName, getClientName(), cron);
            // 注册任务信息
            registerJobInfo(jobName, jobInfo);
        }

    }

    @Override
    public Result<String> executeJob(String jobName, String params) {
        JobInfo jobInfo = AetherContext.getJobInfo(jobName);
        if (jobInfo == null) {
            log.error("根据任务名【{}】找不到任务信息!", jobName);
            return Result.fail("任务【" + jobName + "】不存在!");
        }
        // 异步执行任务
        taskExecutor.execute(() -> {
            ExecJobParam execJobParam = new ExecJobParam();
            execJobParam.setParams(params);
            execJobParam.setExecutorName(getClientName());
            jobInfo.execute(execJobParam);
        });

        return Result.ok("任务【" + jobName + "】分配成功!");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        super.start();
    }

    @Override
    protected void sendJobInfo() {
        Map<String, JobInfo> allJobInfo = getAllJobInfo();
        // 注册任务地址
        String url = serverHost + ServerConstant.JOB_INFO_REGISTER_FULL_PATH;
        String resultStr = HttpUtil.post(url, JSONObject.toJSONString(allJobInfo));
        if (StrUtil.isNotBlank(resultStr)) {
            try {
                JSONObject resultJson = JSONObject.parseObject(resultStr);
                if (resultJson.getBoolean("success")) {
                    log.info("注册任务信息成功!");
                } else {
                    log.warn("注册任务信息失败!");
                }
            } catch (Exception e) {
                log.error("注册任务信息异常：", e);
            }
        }
    }

    @Override
    protected void sendHeartbeat() {
        if (currentPauseHeartbeatCount > 0) {
            // 停止发生心跳就注释，暂停发送心跳就放开
            currentPauseHeartbeatCount--;
            return;
        }
        // 发送心跳请求
        String resultStr = serverClient.sendHeartbeat(getClientName());
        if (StrUtil.isNotBlank(resultStr)) {
            try {
                JSONObject resultJson = JSONObject.parseObject(resultStr);
                // 如果返回执行器不存在，重新注册执行器 (不能重新注册，不然移除逻辑就要重写)
                if (ResultCode.EXECUTOR_NOT_EXIST.equals(resultJson.getInteger("code"))) {
                    log.warn("执行器【{}】不存在，停止发送心跳请求", clientName);
                    currentPauseHeartbeatCount = pauseHeartbeatCount;
                    log.debug("执行器【{}】尝试重新注册", clientName);
                    registerExecutor();
                }

                // 如果返回参数错误，停止\暂停发生心跳请求
                if (ResultCode.PARAMETER_ERROR.equals(resultJson.getInteger("code"))) {
                    log.warn("执行器【{}】心跳请求参数错误，停止发送心跳请求", clientName);
                    currentPauseHeartbeatCount = pauseHeartbeatCount;
                }
            } catch (Exception ex) {
                log.error("发送心跳请求异常：", ex);
            }
        }

        log.debug("执行器【{}】已发送心跳", getClientName());
    }

    private String getClientName() {
        if (StrUtil.isNotBlank(clientName))
            return clientName;

        // 客户端名称
        clientName = context.getEnvironment().getProperty("aether.client.name", "");
        // 如果没指定客户端名称，生成一个uuid
        if (StrUtil.isBlank(clientName))
            clientName = UUID.fastUUID().toString();

        return clientName;
    }

}
