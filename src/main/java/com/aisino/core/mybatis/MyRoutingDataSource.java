package com.aisino.core.mybatis;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.sysmgr.dbinfo.entity.DbInfo;
import com.aisino.base.sysmgr.dbinfo.service.DbInfoService;
import com.aisino.core.util.SpringUtils;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
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
        DbInfo dbInfo = getDbInfoService().getDbInfoByTaxNo(userInfo.getTaxNo());
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
            dataSources.put(userInfo.getId().toString(), dds);
            dataSources2.put(userInfo.getId().toString(), dds);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据用户创建数据源
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
            if (dataSources.get(userInfo.getId().toString()) != null) {
                Map<Object, DataSource> dataSources2 = (Map<Object, DataSource>) resolvedDataSources.get(this);
                dataSources.remove(userInfo.getId().toString());
                dataSources2.remove(userInfo.getId().toString());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从容器中获取Service
     */
    private DbInfoService getDbInfoService() {
        return (DbInfoService) SpringUtils.getBean("dbInfoService");
    }
}
