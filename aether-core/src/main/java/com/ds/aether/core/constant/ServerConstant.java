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
    public static final String EXECUTOR_REGISTER_PATH = SERVER_API + "/executor/register";

}
