package com.aisino.core.mybatis;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.sysmgr.dbinfo.entity.DbInfo;
import com.aisino.base.sysmgr.dbinfo.service.DbInfoService;
import com.aisino.base.sysmgr.dbinfo.service.impl.DbInfoServiceImpl;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by ZhenWeiLai on 2016/11/22.
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    /**
     * 这里可以做简单负载均衡,暂时用不上
     */
//    private int dataSourceOrder;
//    private AtomicInteger count = new AtomicInteger(0);
    public MyRoutingDataSource() {
    }

//    public MyRoutingDataSource(int _dataSourceOrder){
//        this.dataSourceOrder = _dataSourceOrder;
//    }

    @Resource
    private DbInfoService dbInfoService;


    /**
     * 这个方法会根据返回的key去配置文件查找数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getTargetDataSource();
    }


    /**
     * 根据用户创建数据源
     */
    public void addCuzDataSource(UserInfo userInfo) {

        if (StringUtils.isBlank(userInfo.getTaxNo()))
            return;
        DbInfo dbInfo = dbInfoService.getDbInfoByTaxNo(userInfo.getTaxNo());

        String url = "jdbc:mysql://" + dbInfo.getDbaddr() + ":" + dbInfo.getDbport() + "/" + dbInfo.getDbname() + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useSSL=true";
        if (!getCuzDataSourceExist(url,dbInfo.getDbusr(),dbInfo.getDbpwd()))
            return;
        try {
            Field targetDataSources = AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
            Field resolvedDataSources = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
            targetDataSources.setAccessible(true);
            resolvedDataSources.setAccessible(true);
            Map<Object, Object> dataSources = (Map<Object, Object>) targetDataSources.get(this);
            if (dataSources.get(userInfo.getId().toString()) != null)
                return;
            Map<Object, DataSource> dataSources2 = (Map<Object, DataSource>) resolvedDataSources.get(this);
            DruidDataSource dds = new DruidDataSource();
            dds.setUrl("jdbc:mysql://" + dbInfo.getDbaddr() +
                    ":" + dbInfo.getDbport() + "/" + dbInfo.getDbname() + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useSSL=true");
            dds.setUsername(dbInfo.getDbusr());
            dds.setPassword(dbInfo.getDbpwd());
            dataSources.put(userInfo.getUsrno(), dds);
            dataSources2.put(userInfo.getUsrno(), dds);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据用户删除数据源
     */
    public void removeCuzDataSource(UserInfo userInfo) {
        if (StringUtils.isBlank(userInfo.getTaxNo()))
            return;
        try {
            Field targetDataSources = AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
            Field resolvedDataSources = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
            targetDataSources.setAccessible(true);
            resolvedDataSources.setAccessible(true);
            Map<Object, Object> dataSources = (Map<Object, Object>) targetDataSources.get(this);
            if (dataSources.get(userInfo.getUsrno()) != null) {
                Map<Object, DataSource> dataSources2 = (Map<Object, DataSource>) resolvedDataSources.get(this);
                dataSources.remove(userInfo.getUsrno());
                dataSources2.remove(userInfo.getUsrno());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断数据源是否有效
     */
    private boolean getCuzDataSourceExist(String url,String username,String password) {
        Connection conn;
        try {
            Class.forName(DbInfoServiceImpl.mysqlDriver);
            conn = DriverManager.getConnection(url,username,password);
            if (conn!=null){
                conn.close();
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
