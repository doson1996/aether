package com.ds.aether.client.executor;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.client.context.AetherContext;
import com.ds.aether.client.job.JobInfo;
import com.ds.aether.client.job.SpringJobInfo;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.job.Job;
import com.ds.aether.core.model.client.RegisterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author ds
 * @date 2025/4/10
 * @description Spring环境执行器
 */
@Slf4j
public class SpringExecutor extends AbstractExecutor implements ApplicationContextAware, SmartInitializingSingleton {

    @Value("${aether.server.host:localhost:8081}")
    private String serverHost;

    private ApplicationContext context;

    @Override
    public void registerExecutor() {
        // 客户端名称
        String clientName = context.getEnvironment().getProperty("aether.client.name", "");
        // 如果没指定客户端名称，生成一个uuid
        if (StrUtil.isBlank(clientName)) {
            clientName = UUID.fastUUID().toString();
        }
        // 客户端ip
        String clientHost = context.getEnvironment().getProperty("aether.client.host", "");
        // 如果没指定ip，自动获取
        if (StrUtil.isBlank(clientHost)) {
            clientHost = NetUtil.getLocalHostName();
        }
        // 客户端请求端口
        String clientPort = context.getEnvironment().getProperty("server.port", "8080");
        // 客户端请求上下文路径
        String contextPath = context.getEnvironment().getProperty("server.servlet.context-path", "");

        // 构建注册执行器参数
        RegisterParam registerParam = new RegisterParam();
        registerParam.setName(clientName);
        registerParam.setHost(clientHost + clientPort);
        registerParam.setContextPath(contextPath);

        // 注册执行器请求地址
        String registerExecutorUrl = serverHost + ServerConstant.EXECUTOR_REGISTER_PATH;
        // 发送注册请求
        HttpUtil.post(registerExecutorUrl, JSONObject.toJSONString(registerParam));
        log.info("执行器【{}】已注册", clientName);
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
            if (jobAnnotation == null)
                continue;

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

            // 构建任务信息
            JobInfo jobInfo = new SpringJobInfo(jobName, beanName);
            // 注册任务信息
            registerJobInfo(jobName, jobInfo);
        }

    }

    @Override
    public void executeJob(String jobName) {
        JobInfo jobInfo = AetherContext.getJobInfo(jobName);
        if (jobInfo == null) {
            log.error("根据任务名【{}】找不到任务信息!", jobName);
            return;
        }
        jobInfo.execute();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        super.start();
    }

}
