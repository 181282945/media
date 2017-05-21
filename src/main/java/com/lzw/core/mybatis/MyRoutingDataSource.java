package com.lzw.core.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

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
}
