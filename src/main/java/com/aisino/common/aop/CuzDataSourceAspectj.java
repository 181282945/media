package com.aisino.common.aop;

import com.aisino.core.mybatis.DataSourceContextHolder;
import com.aisino.core.security.util.SecurityUtil;
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
        Integer userId = SecurityUtil.getCurrentUserInfo().getId();
        DataSourceContextHolder.setTargetDataSource(userId.toString());
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            DataSourceContextHolder.write();
        }
        return null;
    }
}
