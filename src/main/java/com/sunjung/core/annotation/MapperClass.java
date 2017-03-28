package com.sunjung.core.annotation;

import java.lang.annotation.*;

/**
 * Created by Athos on 2016-07-04.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapperClass{
	/**
	 *指定 MapperClass
	 */
    Class value();
}
