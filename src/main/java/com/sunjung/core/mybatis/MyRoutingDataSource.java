package com.sunjung.core.mybatis;

import com.sunjung.common.handler.CuzDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ZhenWeiLai on 2016/11/22.
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
/**
 * 这里可以做简单负载均衡,暂时用不上
 */
    private final int dataSourceOrder;
    private AtomicInteger count = new AtomicInteger(0);

    public MyRoutingDataSource(int _dataSourceOrder){
        this.dataSourceOrder = _dataSourceOrder;
    }

    /**
     * 这个方法会根据返回的key去配置文件查找数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String targetKey = DataSourceContextHolder.getTargetDataSource();
        try {
            Field field =  MyRoutingDataSource.class.getDeclaredField("targetDataSources");
            field.setAccessible(true);
            Map<Object, Object> dataSources = (Map<Object, Object>) field.get(this);
            dataSources.put("aoczx",new CuzDataSource());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "aoczx";
//        return targetKey;
    }
}
