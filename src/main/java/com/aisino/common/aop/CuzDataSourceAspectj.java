package com.aisino.common.aop;

import com.aisino.core.mybatis.DataSourceContextHolder;
import com.aisino.core.security.util.SecurityUtil;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by 为 on 2017-4-27.
 */
@Component
@Aspect
public class CuzDataSourceAspectj {

    //定义切点
    @Pointcut("@annotation(com.aisino.common.annotation.CuzDataSource)")
    public void cuzDataSource(){}

    @Before("cuzDataSource()")
    public void doBefore(){
        Integer userId = SecurityUtil.getCurrentUserInfo().getId();
        DataSourceContextHolder.setTargetDataSource(userId.toString());
    }

//    @After("cuzDataSource()")
//    public void doAfter(){
//        DataSourceContextHolder.write();
//    }
}
