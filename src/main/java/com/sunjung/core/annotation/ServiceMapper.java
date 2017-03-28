package com.sunjung.core.annotation;

import com.sunjung.core.dao.BaseMapper;
import com.sunjung.core.entity.BaseEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceMapper{
    /**
     * 实体绑定Mapper类
     */
    Class<? extends BaseMapper<?>> value();
}