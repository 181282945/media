package com.aisino.core.entity.annotation;

import java.lang.annotation.*;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 * 表名,以及id
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseEntityMapper {

    /**
     * 数据表名称注解，默认值为类名称
     */
    String tableName();

    /**
     * 默认主键
     */
    String primaryKey() default "id";

    /**
     * 默认枚举分片字段
     */
    String shardingKey() default "shardingId";
}
