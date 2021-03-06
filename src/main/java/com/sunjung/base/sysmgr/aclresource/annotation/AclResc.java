package com.sunjung.base.sysmgr.aclresource.annotation;

import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;

import java.lang.annotation.*;

/**
 * Created by 为 on 2017-4-8.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AclResc {
    String code();
    String name();
    String homePage() default "";
}
