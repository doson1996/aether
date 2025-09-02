package com.ds.aether.server.exception;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.util.StrUtil;
import com.ds.aether.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局异常处理
 *
 * @author ds
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private HttpServletResponse response;

    /**
     * 系统异常捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handlerException(Exception e) {
        log.error("系统异常:", e);
        return Result.fail("系统繁忙!");
    }

    /**
     * 业务异常捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handlerBusinessException(BusinessException e) {
        String msg = "";
        if (StrUtil.isNotBlank(e.getMessage())) {
            msg = e.getMessage();
        }

        return Result.fail(msg);
    }

    /**
     * 未登录异常捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    public void handlerNotLoginException(NotLoginException e) throws IOException {
//        log.error("未登录异常:", e);
        // 未登录，重定向到登录页
        String redirectUrl = "/api/page/login";
        response.sendRedirect(redirectUrl);
    }

    /**
     * 文件上传异常捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handlerMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        String msg = "文件大小超出最大上传限制!";
        return Result.fail(msg);
    }

}
