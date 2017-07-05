package com.aisino.core.service.impl;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.core.dao.BaseShardingMapper;
import com.aisino.core.dto.Pair;
import com.aisino.core.entity.*;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.ShardingSpecification;
import com.aisino.core.service.BaseShardingService;
import com.aisino.core.util.ConstraintUtil;
import com.aisino.core.util.EntityColumnUtil;
import com.aisino.core.util.GenericeClassUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-6-28.
 */
public class BaseShardingServiceImpl<T extends BaseShardingEntity, M extends BaseShardingMapper<T>> implements BaseShardingService<T, M> {

    private Class<?> entityClass = GenericeClassUtils.getSuperClassGenricType(this.getClass(), 0);
    private Class<?> mapperClass = GenericeClassUtils.getSuperClassGenricType(this.getClass(), 1);

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private SqlSessionTemplate sessionTemplate;

    @SuppressWarnings("unchecked")
    protected M getMapper() {
        return (M) sessionTemplate.getMapper(mapperClass);
    }


    @Override
    public T findEntityById(Integer id, Integer shardingId) {
        T entity = getMapper().findEntityById(new ShardingSpecification<T>(entityClass, id, shardingId));
        return entity;
    }

    @Override
    public Integer addEntity(T entity) {
        validateAddEntity(entity);
        getMapper().addEntity(entity);
        return entity.getId();
    }

    /**
     * 子类可以重写新增验证方法
     */
    protected void validateAddEntity(T entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        if (entity.getShardingId() == null)
            entity.setShardingId(cuzSessionAttributes.getEnInfo().getShardingId());
    }

    @Override
    public void updateEntity(T entity, Integer shardingId) {
        entity.setShardingId(shardingId);
        validateUpdateEntity(entity);
        getMapper().updateEntity(entity);
    }

    /**
     * 子类可以重写更新验证方法
     */
    protected void validateUpdateEntity(T entity) {
    }

    @Override
    public void updateEntityEffective(Integer entityId, Integer shardingId) {
        BaseShardingInvoiceEntity entity;
        try {
            entity = (BaseShardingInvoiceEntity)entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("更新状态失败，类型异常。");
        }
        entity.setId(entityId);
        getMapper().updateEntityEffective(new ShardingSpecification<>(entity, shardingId));
    }

    @Override
    public void updateEntityInvalid(Integer entityId, Integer shardingId) {
        BaseShardingInvoiceEntity entity;
        try {
            entity = (BaseShardingInvoiceEntity)entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("更新状态失败，类型异常。");
        }
        entity.setId(entityId);
        getMapper().updateEntityInvalid(new ShardingSpecification<>(entity, shardingId));
    }

    @Override
    public void updateEntityStatus(Integer entityId, String status, Integer shardingId) {
        validateUpdateEntityStatus(entityId, status);
        BaseShardingBusinessEntity entity;
        try {
            entity = (BaseShardingBusinessEntity)entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("更新状态失败，类型异常。");
        }
        entity.setId(entityId);
        entity.setStatus(status);
        getMapper().updateEntityStatus(new ShardingSpecification<>(entity, shardingId),entity);
    }

    protected void validateUpdateEntityStatus(Integer entityId, String status) {

    }

    @Override
    public void deleteById(Integer id, Integer shardingId) {
        getMapper().deleteById(new ShardingSpecification<T>(entityClass, id, shardingId));
    }

    @Override
    public List<T> findAll(Integer shardingId) {
        ShardingSpecification<T> specification = new ShardingSpecification<>(entityClass, shardingId);
        specification.setPageAndSort(new PageAndSort(0, 0, getBaseEntityMapper().primaryKey()));
        return getMapper().findAll(specification);
    }

    @Override
    public List<T> findByLike(ShardingSpecification<T> specification) {
        return getMapper().findByLike(specification);
    }

    @Override
    public T getOne(ShardingSpecification<T> specification) {
        specification.setPageAndSort(new PageAndSort(1, 1));
        List<T> results = this.findByLike(specification);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<T> findByPage(ShardingSpecification<T> specification) {
        Long rowCount = this.findRowCount(specification);
        specification.getPageAndSort().setRowCount(rowCount);
        return getMapper().findByPage(specification);
    }

    @Override
    public List<T> findByLike(T entity, Integer shardingId) {
        ShardingSpecification<T> specification = makeSpecification(null, shardingId);
        List<QueryLike> queryLikes = EntityColumnUtil.setEntityQueryLike(entity, specification.getQueryLikes());
        specification.setQueryLikes(queryLikes);
        return this.findByLike(specification);
    }

    @Override
    public List<T> findByPage(Pair<T, PageAndSort> pair, Integer shardingId) {
        return findByPage(pair.getLeft(), shardingId, pair.getRight());
    }

    @Override
    public List<T> findByPage(T entity, Integer shardingId, PageAndSort pageAndSort) {
        ShardingSpecification<T> specification = makeSpecification(pageAndSort, shardingId);
        List<QueryLike> queryLikes = EntityColumnUtil.setEntityQueryLike(entity, specification.getQueryLikes());
        specification.setQueryLikes(queryLikes);
        return this.findByPage(specification);
    }

    /**
     * 模糊搜索条件
     */
    protected ShardingSpecification<T> makeSpecification(PageAndSort pageAndSort, Integer shardingId) {
        ShardingSpecification<T> specification = new ShardingSpecification<>(entityClass, shardingId);
        specification.setPageAndSort(pageAndSort);
        return specification;
    }


    @Override
    public Long findRowCount(ShardingSpecification<T> specification) {
        return getMapper().findRowCount(specification);
    }

    private BaseEntityMapper getBaseEntityMapper() {
        BaseEntityMapper baseEntityMapper = entityClass.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper) {
            throw new RuntimeException(this.getClass().getSimpleName() + " , 请配置注解 BaseEntityMapper");
        }
        return baseEntityMapper;
    }
}
