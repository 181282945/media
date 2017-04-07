package com.sunjung.core.security.resource.annotation;

import com.sunjung.core.security.resource.entity.ResourceType;

import java.lang.annotation.*;

/**
 * Created by 为 on 2017-4-7.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resc {
    String name();
    String descn();
    ResourceType resourceType();
}
