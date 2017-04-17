package com.sunjung.common.annotation.jqgrid;

import java.lang.annotation.*;

/**
 * Created by 为 on 2017-4-17.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JqgridColumn {
    //表格显示的列名
    String displayName() default "";
    //表格宽度
    String width() default "50";
    //是否允许编辑
    boolean editable() default true;
    //是否查询条件字段
    boolean search() default true;
    //是否隐藏
    boolean hidden() default false;
//    private String displayName;
//    private String name;
//    private String index;
//    private String width;
//    private boolean editable;
//    private boolean search;
//    private boolean hidden;
}
