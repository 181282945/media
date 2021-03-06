package com.sunjung.core.mybatis;

import com.sunjung.core.security.util.SecurityUtil;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by ZhenWeiLai on 2017/4/13.
 */
@Component
@Aspect
public class CuzDataSourceAspectj {

    //定义切点
    @Pointcut("@annotation(com.sunjung.core.mybatis.DataSourceSwitch)")
    public void cuzDataSource(){}

    @Before("cuzDataSource()")
    public void doBefore(){
        Integer userId = SecurityUtil.getCurrentUser().getId();
        DataSourceContextHolder.setTargetDataSource(userId.toString());
    }

    @After("cuzDataSource()")
    public void doAfter(){
        DataSourceContextHolder.write();
    }
}
