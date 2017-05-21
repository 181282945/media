package com.lzw.core.entity.annotation;

import java.lang.annotation.*;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsNotNull {
    String description() default "";
}
