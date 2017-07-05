package com.aisino.base.sysmgr.aclresource.annotation;


import com.aisino.base.sysmgr.aclresource.entity.AclResource;

import java.lang.annotation.*;

/**
 * Created by 为 on 2017-4-8.
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
    //此参数指明该资源属于 前台用户还是后台用户
    AclResource.Target target() default AclResource.Target.USERINFO;
}
