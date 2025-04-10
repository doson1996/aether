package com.ds.aether.core.model;

/**
 * 返回消息
 *
 * @author ds
 */
public final class ResultMsg {

    /**
     * 返回成功消息
     */
    public static final String SUCCESS_MSG = "ok";

    /**
     * 返回失败消息
     */
    public static final String FAIL_MSG = "fail";

    /**------------------------- 成功返回消息 -----------------------------------*/

    /**
     * 创建成功
     */
    public static final String CREATE_SUCCESS_MSG = "创建成功";

    /**
     * 删除成功
     */
    public static final String DELETE_SUCCESS_MSG = "删除成功";

    /**
     * 修改成功
     */
    public static final String UPDATE_SUCCESS_MSG = "修改成功";

    /**
     * 查询成功
     */
    public static final String QUERY_SUCCESS_MSG = "查询成功";


    /**------------------------- 失败返回消息 -----------------------------------*/

    /**
     * 创建失败
     */
    public static final String CREATE_FAIL_MSG = "创建失败";

    /**
     * 删除失败
     */
    public static final String DELETE_FAIL_MSG = "删除失败";

    /**
     * 修改失败
     */
    public static final String UPDATE_FAIL_MSG = "修改失败";

    /**
     * 查询失败
     */
    public static final String QUERY_FAIL_MSG = "查询失败";

    /**
     * 参数错误返回信息
     */
    public static final String PARAMETER_ERROR_MSG = "参数错误";

    /**
     * 参数错误返回信息
     */
    public static final String BUSINESS_EXCEPTION_MSG = "业务异常";

    /**
     * 认证错误返回信息
     */
    public static final String AUTH_EXCEPTION_MSG = "认证异常";
}
