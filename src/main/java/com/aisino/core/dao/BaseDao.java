package com.aisino.core.dao;

import com.aisino.core.entity.BaseEntity;

import java.util.List;

/**
 * Created by 为 on 2017-6-13.
 */
public interface BaseDao<K,V extends BaseEntity> {
    /**
     * 新增用户
     */
    boolean add(V entity);
    /**
     * 批量新增，使用list的方式
     */
    boolean batchAdd(List<V> entitys);
    /**
     * 删除一条数据
     */
    void delete(K key);

    /**
     * 删除多个
     */
    void delete(List<K> keys);
    /**
     * 修改
     */
    boolean update(V entity);
    /**
     * 根据key获取用户
     */
    V get(K keyId);


    void rPushList(K key, V value);

    V lPopList(K key);
}
