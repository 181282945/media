package com.aisino.core.security;

import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.util.RequestUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 为 on 2017-5-27.
 */
@RestController
public class InvalidSessionHandler {

    // Session超时处理
    @RequestMapping(value = "/invalidSession", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE,MediaType.TEXT_HTML_VALUE})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object invalidSession(HttpServletRequest request) {
        if (RequestUtil.isAjaxRequest(request)) {
            return ResultDataDto.addOperationFailure("登录超时,请重新登录!").setDatas("/index");
        } else {
            ModelAndView mav = new ModelAndView("/index");
            return mav;
        }
    }
}
