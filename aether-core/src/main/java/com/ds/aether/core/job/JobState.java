package com.ds.aether.core.job;

/**
 * @author ds
 * @date 2025/8/12
 * @description
 */
public class JobState {

    /**
     * 未执行
     */
    public static final Integer NOT_EXECUTED = 0;

    /**
     * 运行中
     */
    public static final Integer RUNNING = 1;

    /**
     * 运行完成
     */
    public static final Integer COMPLETED = 2;

    /**
     * 执行异常
     */
    public static final Integer ERROR = 3;

    /**
     * 已取消
     */
    public static final Integer CANCEL = 4;

    /**
     * 禁用
     */
    public static final Integer DISABLED = 5;

}
