package com.aisino.core.dao;

import com.aisino.core.dao.impl.BaseShardingProvider;
import com.aisino.core.entity.*;
import com.aisino.core.mybatis.specification.ShardingSpecification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by 为 on 2017-6-28.
 */
public interface BaseShardingMapper <T extends BaseShardingEntity> {


    //---------------------------------------------------------
    /**
     * 根据Id查询实体
     */
    @SelectProvider(type = BaseShardingProvider.class, method = "findEntityById")
    T findEntityById(ShardingSpecification<T> specification);

    /**
     * 新增实体
     */
    @InsertProvider(type = BaseShardingProvider.class, method = "addEntity")
    @Options(useGeneratedKeys = true)
    void addEntity(T entity);

    /**
     * 新增实体
     */
    @InsertProvider(type = BaseShardingProvider.class, method = "addBatchEntity")
    @Options(useGeneratedKeys = true)
    void addBatchEntity(T entity);
    /**
     * 更新实体
     */
    @UpdateProvider(type = BaseShardingProvider.class, method = "updateEntity")
    void updateEntity(T entity);

    /**
     * 更新状态
     */
    @UpdateProvider(type = BaseShardingProvider.class, method = "updateEntityStatus")
    <S extends BaseShardingBusinessEntity> void updateEntityStatus(ShardingSpecification<S> shardingSpecification,S entity);

    /**
     * 更新状态为失效
     */
    @UpdateProvider(type = BaseShardingProvider.class, method = "updateEntityInvalid")
    <S extends BaseShardingInvoiceEntity> void updateEntityInvalid(ShardingSpecification<S> shardingSpecification);


    /**
     * 更新状态为有效
     */
    @UpdateProvider(type = BaseShardingProvider.class, method = "updateEntityEffective")
    <S extends BaseShardingInvoiceEntity> void updateEntityEffective(ShardingSpecification<S> shardingSpecification);

    /**
     * 根据Id删除实体
     */
    @DeleteProvider(type = BaseShardingProvider.class, method = "deleteById")
    void deleteById(ShardingSpecification<T> specification);

    /**
     * 查询所有数据
     */
    @SelectProvider(type = BaseShardingProvider.class, method = "findAll")
    List<T> findAll(ShardingSpecification<T> specification);

    /**
     * 单表模糊查询，暂不支持链接查询
     */
    @SelectProvider(type = BaseShardingProvider.class, method = "findByLike")
    List<T> findByLike(ShardingSpecification<T> specification);

    /**
     * 单表模糊分页查询，暂不支持链接查询
     */
    @SelectProvider(type = BaseShardingProvider.class, method = "findByPage")
    List<T> findByPage(ShardingSpecification<T> specification);

    /**
     * 单表模糊查询总记录数
     */
    @SelectProvider(type = BaseShardingProvider.class, method = "findRowCount")
    long findRowCount(ShardingSpecification<T> specification);

}
