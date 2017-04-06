package com.sunjung.common.usersigninfo.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created by ZhenWeiLai on 2017/4/6.
 * 别名注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface CustomAlias {
    String value();
}
