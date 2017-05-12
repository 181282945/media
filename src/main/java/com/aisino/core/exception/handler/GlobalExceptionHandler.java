package com.aisino.core.exception.handler;

import com.aisino.core.dto.ResultDataDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 为 on 2017-5-11.
 * 全局异常统一处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultDataDto jsonErrorHandler(Exception ex) throws Exception {
        ResultDataDto resultDataDto = new ResultDataDto(ex);
        return resultDataDto;
    }
}
