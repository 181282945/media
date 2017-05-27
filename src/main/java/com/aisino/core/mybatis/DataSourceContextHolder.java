package com.aisino.core.mybatis;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.core.util.SpringUtils;


/**
 * Created by ZhenWeiLai on 2016/11/22.
 * 多数据源/读写分离
 */
public class DataSourceContextHolder {

    private static boolean isDbexist(){
      return  getCuzSessionAttributes().isDbexist();
    }

    private static CuzSessionAttributes getCuzSessionAttributes(){
        return  (CuzSessionAttributes)SpringUtils.getBean("cuzSessionAttributes");
    }


    private static final ThreadLocal<String> dataSourceLocal = new ThreadLocal<>();

    public static ThreadLocal<String> getDataSourceLocal() {
        return dataSourceLocal;
    }


    /**
     * 主库 只有一个
     */
    public static void write() {
        dataSourceLocal.set(TargetDataSource.WRITE.getCode());
    }

    /**
     * 从库 可以有多个
     */
    public static void read() {
        dataSourceLocal.set(TargetDataSource.READ.getCode());
    }


    /**
     * 主库 只有一个
     */
    public static void info() {
        dataSourceLocal.set(TargetDataSource.INFO.getCode());
    }

    /**
     * 用户库
     */
    public static void user() {

        if(!isDbexist())
            throw new RuntimeException("用户数据库不存在!请联系管理员!");
        String userInfo = getCuzSessionAttributes().getUserInfo().getUsrno();
        if (userInfo != null)
            dataSourceLocal.set(userInfo);
    }

    public static String getTargetDataSource() {
        return dataSourceLocal.get();
    }
}
