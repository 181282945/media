package com.lzw.core.util;

import com.lzw.core.entity.annotation.DefaultValue;
import com.lzw.core.entity.annotation.IsNotNull;

import java.lang.reflect.Field;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public class ConstraintUtil {


    //判断非空属性是否有值,没有的话抛出异常
    public static void isNotNullConstraint(Object obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            IsNotNull isNotNull = field.getAnnotation(IsNotNull.class);
            try {
                if(null!=isNotNull && field.get(obj) == null){
                    throw new RuntimeException(isNotNull.description()+"不能为空!");
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("非法参数!");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("参数不能访问!");
            }
        }
    }

    //设置默认值
    public static void setDefaultValue(Object obj){
        for(Class<?> clazz = obj.getClass();clazz != Object.class;clazz = clazz.getSuperclass()){
            for(Field field : clazz.getDeclaredFields()){
                field.setAccessible(true);
                DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
                try {
                    if(defaultValue != null && field.get(obj) == null){
                        if(field.getType().toString().equals("class java.lang.String")){
                            field.set(obj, defaultValue.value());
                        }else if(field.getType().toString().equals("class java.lang.Integer")){
                            field.set(obj, Integer.parseInt(defaultValue.value()));
                        }else if(field.getType().toString().equals("class java.lang.Double")){
                            field.set(obj, Double.parseDouble(defaultValue.value()));
                        }else if(field.getType().toString().equals("class java.lang.Boolean")){
                            field.set(obj,Boolean.valueOf(defaultValue.value()));
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
