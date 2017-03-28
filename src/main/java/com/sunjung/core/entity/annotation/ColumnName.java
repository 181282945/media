package com.sunjung.core.entity.annotation;

import java.lang.annotation.*;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 * 列名,当属性名与数据表字段名不一样时使用
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnName {

    /**
     * 数据表列名,默认为属性名
     */
    String columnName();
}
