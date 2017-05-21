package com.lzw.base.sysmgr.aclresource.annotation;


import java.lang.annotation.*;

/**
 * Created by ZhenWeiLai on on 2016-10-16.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AclResc {
    int id();//ACLResource 因为特殊原因不使用 id 自动增长,所以必须自定义ID ,并且不能重复
    String code();
    String name();
    String homePage() default "";
    boolean isMenu() default true;
}
