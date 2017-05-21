package com.lzw.core.exception.handler;

import com.lzw.core.dto.ResultDataDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by 为 on 2017-5-11.
 * 全局异常统一处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDataDto jsonErrorHandler(Exception ex) throws Exception {
        ResultDataDto resultDataDto = new ResultDataDto(ex);
        return resultDataDto;
    }
}
