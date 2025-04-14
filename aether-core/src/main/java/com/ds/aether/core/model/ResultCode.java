package com.ds.aether.core.model;

/**
 * 返回状态码
 *
 * @author ds
 */
public final class ResultCode {

    /**
     * 成功
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败
     */
    public static final Integer FAIL = 900;

    /**
     * 参数错误
     */
    public static final Integer PARAMETER_ERROR = 901;

    /**
     * 执行器不存在
     */
    public static final Integer EXECUTOR_NOT_EXIST = 902;

}
