package com.ds.aether.client.executor;

import java.util.Map;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ds.aether.core.constant.ServerConstant;
import com.ds.aether.core.job.Job;
import com.ds.aether.core.model.RegisterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

/**
 * @author ds
 * @date 2025/4/10
 * @description Spring环境执行器
 */
@Slf4j
public class SpringExecutor extends AbstractExecutor implements ApplicationContextAware, SmartInitializingSingleton {

    @Value("${aether.server.host:localhost:8081}")
    private String serverHost;

    @Value("${aether.client.host:localhost:8082}")
    private String clientHost;

    @Value("${aether.client.name:executor1}")
    private String clientName;

    private ApplicationContext context;

    @Override
    public void registerExecutor() {
        String url = serverHost + ServerConstant.EXECUTOR_REGISTER_PATH;
        // 客户端请求端口
        String clientPort = context.getEnvironment().getProperty("server.port", "8080");
        // 客户端请求上下文路径
        String contextPath = context.getEnvironment().getProperty("server.servlet.context-path", "");

        RegisterParam registerParam = new RegisterParam();
        registerParam.setName(clientName);
        registerParam.setHost(clientHost);

        HttpUtil.post(url, JSONObject.toJSONString(registerParam));
        log.info("执行器【{}】已注册", clientName);
    }

    @Override
    public void initJobs() {
        if (context == null) {
            return;
        }
        // 拿到所有带@Job的bean
        Map<String, Object> jobBeans = context.getBeansWithAnnotation(Job.class);
        if (CollectionUtils.isEmpty(jobBeans)) {
            return;
        }

        jobBeans.forEach((beanName, bean) -> {
            System.out.println("beanName = " + beanName + bean);
        });

    }

    @Override
    public void executeJob(String jobName) {

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
