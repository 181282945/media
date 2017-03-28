package com.sunjung.core.dto;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import com.sunjung.core.util.EntityColumnUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件集合
 */
public class CriteriaDto<T extends BaseEntity> {

    public CriteriaDto() {
        super();
    }

    public CriteriaDto(Class<?> entityClass) {
        super();
        BaseEntityMapper baseEntityMapper = entityClass.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper) {
            throw new RuntimeException("实体："+entity.getClass().getName()+"  ,  请配置注解BaseEntityMapper");
        }
        this.tableName = baseEntityMapper.tableName();
        this.primaryKey = baseEntityMapper.primaryKey();
    }

    public CriteriaDto(Class<?> entityClass, String entityId) {
        super();
        BaseEntityMapper baseEntityMapper = entityClass.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper) {
            throw new RuntimeException("实体："+entity.getClass().getName()+"  ,  请配置注解BaseEntityMapper");
        }
        this.tableName = baseEntityMapper.tableName();
        this.primaryKey = baseEntityMapper.primaryKey();
        this.entityId = entityId;
    }

    public CriteriaDto(T entity) {
        super();
        this.entity = entity;
        this.entityId = entity.getId()+"";
        BaseEntityMapper baseEntityMapper = entity.getClass().getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper) {
            throw new RuntimeException("实体："+entity.getClass().getName()+"  ,  请配置注解BaseEntityMapper");
        }
        this.tableName = baseEntityMapper.tableName();
        this.primaryKey = baseEntityMapper.primaryKey();
        this.queryLikes = EntityColumnUtil.generateEntityQueryLike(entity);
    }

    /**
     * 实体，动态反射查询条件（表名、属性、类型）
     */
    private T entity;

    /**
     * 主键列名
     */
    private String primaryKey;

    /**
     * 实体Id
     */
    private String entityId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 查询条件集合
     */
    private List<QueryLike> queryLikes = new ArrayList<QueryLike>();

    /**
     * Group By 列名，多个以,间隔
     */
    private String groupByName;

    /**
     * 分页、排序条件
     */
    private PageAndSortDto pageAndSortDto;

    // -------------------------- getter and setter -----------------------------

    public CriteriaDto<T> addQueryLike(QueryLike queryLike){
        this.getQueryLikes().add(queryLike);
        return this;
    }
    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<QueryLike> getQueryLikes() {
        return queryLikes;
    }

    public void setQueryLikes(List<QueryLike> queryLikes) {
        this.queryLikes = queryLikes;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public PageAndSortDto getPageAndSortDto() {
        return pageAndSortDto;
    }

    public void setPageAndSortDto(PageAndSortDto flexiPageDto) {
        this.pageAndSortDto = flexiPageDto;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getGroupByName() {
        return groupByName;
    }

    public void setGroupByName(String groupByName) {
        this.groupByName = groupByName;
    }

}
