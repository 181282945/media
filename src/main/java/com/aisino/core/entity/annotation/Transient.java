package com.aisino.core.entity.annotation;

import java.lang.annotation.*;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 * 标记字段为瞬时,非持久化
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transient {
}
