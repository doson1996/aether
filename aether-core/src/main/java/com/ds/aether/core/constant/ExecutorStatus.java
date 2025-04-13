package com.ds.aether.core.constant;

/**
 * @author ds
 * @date 2025/4/14
 * @description 执行器状态 0.离线 1.在线 2.执行中 3.执行失败
 */
public class ExecutorStatus {

    /**
     * 离线
     */
    public static final Integer OFFLINE = 0;

    /**
     * 在线
     */
    public static final Integer ONLINE = 1;

    /**
     * 执行中
     */
    public static final Integer EXECUTING = 2;

    /**
     * 执行失败
     */
    public static final Integer FAIL = 3;

}
