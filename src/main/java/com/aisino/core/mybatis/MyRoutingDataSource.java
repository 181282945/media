package com.aisino.core.mybatis;

import com.aisino.core.entity.BaseEntity;
import com.alibaba.druid.pool.DruidDataSource;
import com.aisino.base.sysmgr.acluser.entity.AclUser;
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

    public MyRoutingDataSource(){
    }

//    public MyRoutingDataSource(int _dataSourceOrder){
//        this.dataSourceOrder = _dataSourceOrder;
//    }

    /**
     * 这个方法会根据返回的key去配置文件查找数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getTargetDataSource();
    }


    /**
     * 根据用户创建数据源
     */
    public void addCuzDataSource(BaseEntity entity){
        try {
            Field targetDataSources =  AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
            Field resolvedDataSources =  AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
            targetDataSources.setAccessible(true);
            resolvedDataSources.setAccessible(true);
            Map<Object, Object> dataSources = (Map<Object, Object>) targetDataSources.get(this);
            Map<Object, DataSource> dataSources2 = (Map<Object, DataSource>) resolvedDataSources.get(this);
            DruidDataSource dds = new DruidDataSource();
            dds.setUsername("root");
            dds.setUrl("jdbc:mysql://localhost:3306/springboot_demo_slave?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useSSL=true");
            dds.setPassword("123");
            dataSources.put(entity.getId().toString(),dds);
            dataSources2.put(entity.getId().toString(),dds);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
