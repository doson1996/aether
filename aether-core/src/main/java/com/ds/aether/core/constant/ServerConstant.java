package com.ds.aether.core.constant;

import com.ds.aether.core.context.SpringContext;

/**
 * @author ds
 * @date 2025/4/11 0:12
 * @description 服务端常量
 */
public class ServerConstant extends AetherConstant {

    /**
     * 服务端请求上下文路径(默认: /api)
     */
    public static final String SERVER_API = SpringContext.getContext().getEnvironment().getProperty("aether.server.context-path", "/api");

    /**
     * 服务端执行器相关api请求路径
     */
    public static final String EXECUTOR_PATH = "/executor";

    /**
     * 注册执行器请求路径
     */
    public static final String EXECUTOR_REGISTER_PATH = SERVER_API + EXECUTOR_PATH + "/register";

    /**
     * 服务端心跳请求路径
     */
    public static final String CLIENT_HEARTBEAT_PATH = SERVER_API + EXECUTOR_PATH + "/heartbeat";

    /**
     * 服务端任务信息相关api请求路径
     */
    public static final String JOB_INFO_PATH = "/job-info";

    /**
     * 注册任务信息请求路径
     */
    public static final String JOB_INFO_REGISTER_PATH = "/register";

    /**
     * 注册任务信息请求全路径
     */
    public static final String JOB_INFO_REGISTER_FULL_PATH = SERVER_API + JOB_INFO_PATH + JOB_INFO_REGISTER_PATH;

}
