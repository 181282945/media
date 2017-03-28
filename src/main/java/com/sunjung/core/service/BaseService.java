package com.sunjung.core.service;

import com.sunjung.core.dto.CriteriaDto;
import com.sunjung.core.dto.PageAndSortDto;
import com.sunjung.core.dto.Pair;
import com.sunjung.core.entity.BaseEntity;

import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 * 基础服务接口
 */
public interface BaseService<T extends BaseEntity> {

    /**
     * 根据Id查询实体
     */
    T findEntityById(String id);

    /**
     * 新增实体
     * @return 新增实体Id
     */
    Integer addEntity(T entity);

    /**
     * 更新实体
     */
    void updateEntity(T entity);

    /**
     * 更新实体状态
     */
    void updateEntityStatus(String entityId, String status);

    /**
     * 根据Id删除实体
     */
    void deleteById(String id);

    /**
     * 查询所有数据
     */
    List<T> findAll();

    /**
     * 单表模糊查询
     */
    List<T> findByLike(CriteriaDto<T> criteriaDto);

    /**
     * 根据模糊分页查询
     */
    List<T> findByPage(CriteriaDto<T> criteriaDto);

    /**
     * 根据实体模糊查询
     */
    List<T> findByLike(T entity);

    /**
     * 根据实体模糊分页查询
     */
    List<T> findByPage(Pair<T, PageAndSortDto> pair);

    /**
     * 根据实体模糊分页查询
     */
    List<T> findByPage(T entity, PageAndSortDto pageAndSortDto);

    /**
     * 单表模糊查询总记录数
     */
    Long findRowCount(CriteriaDto<T> criteriaDto);

    /**
     * 查找上一条
     */
    T getEntityPreviousById(String id);
    /**
     * 查找下一条
     */
    T getEntityNextById(String id);
}
