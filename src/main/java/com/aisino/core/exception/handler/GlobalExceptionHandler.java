package com.aisino.core.exception.handler;

import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.util.RequestUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 为 on 2017-5-11.
 * 全局异常统一处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(Exception ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
         /*判断请求类型是不是ajax的*/
        if (RequestUtil.isAjaxRequest(request)) {
            ResultDataDto resultDataDto = new ResultDataDto(ex);
            return resultDataDto;
        } else {
          /*如果不是Ajax的重从定向到错误界面*/
            ModelAndView mav = new ModelAndView("/error");
            mav.addObject("message", ex.getMessage());
            return mav;
        }
    }
}
