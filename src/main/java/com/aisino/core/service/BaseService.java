package com.aisino.core.service;

import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.dao.BaseMapper;
import com.aisino.core.dto.Pair;
import com.aisino.core.entity.BaseEntity;
import com.aisino.core.mybatis.specification.Specification;

import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 * 基础服务接口
 */
public interface BaseService<T extends BaseEntity,M extends BaseMapper> {

    /**
     *  查询一个
     */
    T getOne(Specification<T> specification);

    /**
     * 根据Id查询实体
     */
    T findEntityById(Integer id);

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
     * 更新实体为失效状态
     * @param entityId
     */
    void updateEntityInvalid(Integer entityId);


    /**
     * 更新实体为有效状态
     * @param entityId
     */
    void updateEntityEffective(Integer entityId);

    /**
     * 根据Id删除实体
     */
    void deleteById(Integer id);

    /**
     * 查询所有数据
     */
    List<T> findAll();

    /**
     * 单表模糊查询
     */
    List<T> findByLike(Specification<T> specification);

    /**
     * 根据模糊分页查询
     */
    List<T> findByPage(Specification<T> specification);

    /**
     * 根据实体模糊查询
     */
    List<T> findByLike(T entity);

    /**
     * 根据实体模糊分页查询
     */
    List<T> findByPage(Pair<T, PageAndSort> pair);

    /**
     * 根据实体模糊分页查询
     */
    List<T> findByPage(T entity, PageAndSort pageAndSort);

    /**
     * 根据JqgridFilters模糊分页查询
     */
    List<T> findByJqgridFilters(JqgridFilters jqgridFilters, PageAndSort pageAndSort);

    /**
     * 单表模糊查询总记录数
     */
    Long findRowCount(Specification<T> specification);

    /**
     * 查找上一条
     */
    T getEntityPreviousById(Integer id);
    /**
     * 查找下一条
     */
    T getEntityNextById(Integer id);
}
