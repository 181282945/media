package com.aisino.common.aop;

import com.aisino.core.mybatis.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by 为 on 2017-4-27.
 */
@Component
@Aspect
@Order(0)
public class CuzDataSourceAspectj {

    //定义切点
    @Pointcut("@annotation(com.aisino.common.annotation.CuzDataSource)")
    public void cuzDataSource(){}

    @Around("cuzDataSource()")
    public Object aroundCuzDataSource(ProceedingJoinPoint joinPoint){
        DataSourceContextHolder.user();
        Object result = null;
        try {
            result = joinPoint.proceed();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            DataSourceContextHolder.write();
        }
        return result;
    }
}
