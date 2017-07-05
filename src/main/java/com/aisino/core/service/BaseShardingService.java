package com.aisino.core.service;

import com.aisino.core.dao.BaseShardingMapper;
import com.aisino.core.dto.Pair;
import com.aisino.core.entity.BaseShardingEntity;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.ShardingSpecification;

import java.util.List;

/**
 * Created by 为 on 2017-6-28.
 */
public interface BaseShardingService <T extends BaseShardingEntity,M extends BaseShardingMapper> {

    /**
     *  查询一个
     */
    T getOne(ShardingSpecification<T> specification);

    /**
     * 根据Id查询实体
     */
    T findEntityById(Integer id,Integer shardingId);

    /**
     * 新增实体
     * @return 新增实体Id
     */
    Integer addEntity(T entity);

    /**
     * 更新实体
     */
    void updateEntity(T entity,Integer shardingId);

    /**
     * 更新实体状态
     */
    void updateEntityStatus(Integer entityId, String status,Integer shardingId);

    /**
     * 更新实体为失效状态
     * @param entityId
     */
    void updateEntityInvalid(Integer entityId,Integer shardingId);


    /**
     * 更新实体为有效状态
     * @param entityId
     */
    void updateEntityEffective(Integer entityId,Integer shardingId);

    /**
     * 根据Id删除实体
     */
    void deleteById(Integer id,Integer shardingId);

    /**
     * 查询所有数据
     */
    List<T> findAll(Integer shardingId);

    /**
     * 单表模糊查询
     */
    List<T> findByLike(ShardingSpecification<T> specification);

    List<T> findByPage(ShardingSpecification<T> specification);

    List<T> findByLike(T entity,Integer shardingId);

    List<T> findByPage(Pair<T, PageAndSort> pair,Integer shardingId);

    List<T> findByPage(T entity,Integer shardingId, PageAndSort pageAndSort);

    /**
     * 单表模糊查询总记录数
     */
    Long findRowCount(ShardingSpecification<T> specification);
}
