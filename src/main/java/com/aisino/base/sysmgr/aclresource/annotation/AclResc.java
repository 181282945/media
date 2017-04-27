package com.aisino.base.sysmgr.aclresource.annotation;

import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;

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
    //此参数指明该资源属于 前台用户还是后台用户
    AclResourceTarget target() default AclResourceTarget.USERINFO;
}
