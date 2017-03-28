package com.sunjung.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 * 泛型工具类
 */
public class GenericeClassUtils {
    /**
     * 根据参数类,获取接口泛型具体参数,返回具体类型
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return null;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return null;
        }

        if (!(params[index] instanceof Class<?>)) {
            return null;
        }
        return (Class<?>) params[index];
    }
}
