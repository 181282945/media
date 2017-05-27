package com.aisino.core.security;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-5-27.
 */
@RestController
public class InvalidSessionHandler {

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    // Session超时处理
    @RequestMapping(value = "/invalidSession", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ModelAndView invalidSession() {

        if (cuzSessionAttributes.getUserInfo() != null) {
            ModelAndView mav = new ModelAndView("invalid");
            mav.addObject("message", "登录超时,请重新登录!");
            return mav;
        }
        return new ModelAndView("index");
    }
}
