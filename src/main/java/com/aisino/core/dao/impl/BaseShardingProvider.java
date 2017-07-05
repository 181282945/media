package com.aisino.core.dao.impl;

import com.aisino.core.entity.*;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.ShardingSpecification;
import com.aisino.core.util.EntityColumnUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by 为 on 2017-6-28.
 */
public class BaseShardingProvider<T extends BaseShardingEntity> {

    public final static String TABLENAME_KEY = "tablename";

    /**
     * 根据Id查询实体
     */
    public String findEntityById(ShardingSpecification<T> specification) {
        return "SELECT * FROM " + specification.getTableName() + " WHERE " + specification.getPrimaryKey() + " = " + specification.getEntityId() + " AND " + specification.getShardingKey() + " = " + specification.getShardingId();
    }

    /**
     * 根据Id查询实体
     */
    public String findAll(ShardingSpecification<T> specification) {
        StringBuilder sqlString = new StringBuilder();
        sqlString.append("select * from " + specification.getTableName() + " where " + specification.getShardingKey() + " = " + specification.getShardingId());
        PageAndSort pageAndSort = specification.getPageAndSort();
        if (null != pageAndSort && null != pageAndSort.getSortName() && pageAndSort.getSortName().length() > 0) {
            sqlString.append(" order by " + pageAndSort.getSortName() + " " + pageAndSort.getSortOrder());
        }
        return sqlString.toString();
    }

    /**
     * 新增实体
     */
    public String addEntity(T entity) {
        entity.setInsertDate(new Date());
        List<QueryLike> queryLikes = EntityColumnUtil.generateEntityQueryLike(entity);

        StringBuilder sql = new StringBuilder();
        sql.append(" insert into " + getTableNameThrowException(entity.getClass()) + " (");
        int index = 1;
        for (QueryLike queryLike : queryLikes) {
            if (queryLike.getIsTransient()) {
                continue;
            }
            sql.append(queryLike.getColumnName());
            if (index < queryLikes.size()) {
                sql.append(" , ");
            }
            index++;
        }
        if (sql.toString().lastIndexOf(" , ") == sql.toString().length() - 3) {
            sql = new StringBuilder(sql.toString().substring(0, sql.toString().lastIndexOf(" , ")));
        }
        sql.append(") values (");
        index = 1;
        for (QueryLike queryLike : queryLikes) {
            if (queryLike.getIsTransient()) {
                continue;
            }
            sql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue().replaceAll("'", "\\\\\'") : "'" + queryLike.getValue().replaceAll("'", "\\\\\'") + "'");
            if (index < queryLikes.size()) {
                sql.append(" , ");
            }
            index++;
        }
        if (sql.toString().lastIndexOf(" , ") == sql.toString().length() - 3) {
            sql = new StringBuilder(sql.toString().substring(0, sql.toString().lastIndexOf(" , ")));
        }
        sql.append("); ");
        return sql.toString();
    }

    /**
     * 新增实体
     */
    public String addBatchEntity(List<T> entitys) {

        List<QueryLike> queryLikes = EntityColumnUtil.generateEntityQueryLike(entitys.get(0));

        StringBuilder sql = new StringBuilder();
        sql.append(" insert into " + getTableNameThrowException(entitys.get(0).getClass()) + " (");
        int index = 1;
        for (QueryLike queryLike : queryLikes) {
            if (queryLike.getIsTransient()) {
                continue;
            }
            sql.append(queryLike.getColumnName());
            if (index < queryLikes.size()) {
                sql.append(" , ");
            }
            index++;
        }
        if (sql.toString().lastIndexOf(" , ") == sql.toString().length() - 3) {
            sql = new StringBuilder(sql.toString().substring(0, sql.toString().lastIndexOf(" , ")));
        }

        for (T entity : entitys) {
            index = 1;
            sql.append(") values (");
            entity.setInsertDate(new Date());
            List<QueryLike> subQueryLikes = EntityColumnUtil.generateEntityQueryLike(entity);
            for (QueryLike queryLike : subQueryLikes) {
                if (queryLike.getIsTransient()) {
                    continue;
                }
                sql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue().replaceAll("'", "\\\\\'") : "'" + queryLike.getValue().replaceAll("'", "\\\\\'") + "'");
                if (index < queryLikes.size()) {
                    sql.append(" , ");
                }
                index++;
            }
        }

        if (sql.toString().lastIndexOf(" , ") == sql.toString().length() - 3) {
            sql = new StringBuilder(sql.toString().substring(0, sql.toString().lastIndexOf(" , ")));
        }
        sql.append("); ");
        return sql.toString();
    }

    /**
     * 更新实体
     */
    public String updateEntity(T entity) {
        List<QueryLike> queryLikes = EntityColumnUtil.generateEntityQueryLike(entity);

        BaseEntityMapper baseEntityMapper = getBaseEntityMapperThrowException(entity.getClass());

        StringBuilder sql = new StringBuilder();
        sql.append("update " + baseEntityMapper.tableName() + " set ");

        int index = 1;
        for (QueryLike queryLike : queryLikes) {
            if (queryLike.getIsTransient()) {
                continue;
            }
            sql.append(queryLike.getColumnName() + " = ");
            sql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue().replaceAll("'", "\\\\\'") : "'" + queryLike.getValue().replaceAll("'", "\\\\\'") + "'");
            if (index < queryLikes.size()) {
                sql.append(" , ");
            }
            index++;
        }
        if (sql.toString().lastIndexOf(" , ") == sql.toString().length() - 3) {
            sql = new StringBuilder(sql.toString().substring(0, sql.toString().lastIndexOf(" , ")));
        }

        sql.append(" where " + baseEntityMapper.primaryKey() + " = " + entity.getId() + " AND " + baseEntityMapper.shardingKey() + " = " + entity.getShardingId());
        return sql.toString();
    }

    /**
     * 更新实体状态
     */
    public <S extends BaseShardingBusinessEntity> String updateEntityStatus(ShardingSpecification<S> shardingSpecification, S entity) {
        return "update " + getTableNameThrowException(entity.getClass()) + " set status = '" + entity.getStatus() + "' where id = " + entity.getId() + " AND " + shardingSpecification.getShardingKey() + " = " + shardingSpecification.getShardingId();
    }

    /**
     * 更新实体为失效
     */
    public <S extends BaseShardingInvoiceEntity> String updateEntityInvalid(ShardingSpecification<S> shardingSpecification) {
        return "update " + getTableNameThrowException(shardingSpecification.getEntity().getClass()) + " set delflags = true where id = " + shardingSpecification.getEntityId()+ " AND " + shardingSpecification.getShardingKey() + " = " + shardingSpecification.getShardingId();
    }

    /**
     * 更新实体为生效
     */
    public <S extends BaseShardingInvoiceEntity> String updateEntityEffective(ShardingSpecification<S> shardingSpecification) {
        return "update " + getTableNameThrowException(shardingSpecification.getEntity().getClass()) + " set delflags = false where id = " + shardingSpecification.getEntityId() + " AND " + shardingSpecification.getShardingKey() + " = " + shardingSpecification.getShardingId();
    }

    /**
     * 根据Id删除实体
     */
    public String deleteById(ShardingSpecification<T> shardingSpecification) {
        return "delete from " + shardingSpecification.getTableName() + " where id = " + shardingSpecification.getEntityId() + " AND " + shardingSpecification.getShardingKey() + " = " + shardingSpecification.getShardingId();
    }

    /**
     * 单表模糊查询，暂不支持链接查询
     */
    public String findByLike(ShardingSpecification<T> shardingSpecification) {

        if (null == shardingSpecification.getTableName()) {
            throw new RuntimeException("没有设置查询表名：tablename");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select * from " + shardingSpecification.getTableName());
        sql.append(generateWhereSql(shardingSpecification));
        sql.append(generateGroupBySql(shardingSpecification));
        return sql.toString();
    }

    /**
     * 单表模糊分页查询，暂不支持链接查询
     */
    public String findByPage(ShardingSpecification<T> shardingSpecification) {
        StringBuffer sql = new StringBuffer();
        sql.append(findByLike(shardingSpecification));
        sql.append(generateLimitOrderSql(shardingSpecification.getPageAndSort()));
        return sql.toString();
    }

    /**
     * 单表模糊查询，暂不支持链接查询
     */
    public String findRowCount(ShardingSpecification<T> shardingSpecification) {

        if (null == shardingSpecification.getTableName()) {
            throw new RuntimeException("没有设置查询表名：tablename");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select count(0) from " + shardingSpecification.getTableName());
        sql.append(generateWhereSql(shardingSpecification));
        sql.append(generateGroupBySql(shardingSpecification));
        return sql.toString();
    }

    // ------------------------------------------- private ------------------------------------------------


    /**
     * 根据实体注解，获取表名，如果没有则抛出异常
     */
    private String getTableNameThrowException(Class<?> cls) {
        BaseEntityMapper baseEntityMapper = cls.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper || null == baseEntityMapper.tableName() || 0 == baseEntityMapper.tableName().trim().length()) {
            throw new RuntimeException("实体：" + cls.getName() + "  ,  请配置注解BaseEntityMapper");
        }
        return baseEntityMapper.tableName();
    }

    private BaseEntityMapper getBaseEntityMapperThrowException(Class<?> cls) {
        BaseEntityMapper baseEntityMapper = cls.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper || null == baseEntityMapper.tableName() || 0 == baseEntityMapper.tableName().trim().length()) {
            throw new RuntimeException("实体：" + cls.getName() + "  ,  请配置注解BaseEntityMapper");
        }
        return baseEntityMapper;
    }

    /**
     * 生成动态查询条件
     */
    private String generateWhereSql(ShardingSpecification<T> shardingSpecification) {
        List<QueryLike> queryLikes = shardingSpecification.getQueryLikes();
        if (null == queryLikes || queryLikes.isEmpty()) {
            return " where " + shardingSpecification.getShardingKey() + " = " + shardingSpecification.getShardingId();
        }
//        boolean isnull = true;
        StringBuffer whereSql = new StringBuffer();
        whereSql.append(" where " + shardingSpecification.getShardingKey() + " = " + shardingSpecification.getShardingId()+" ");
        for (QueryLike queryLike : queryLikes) {
//            if (ValidateUtil.isEmpty("columnName", queryLike) || ValidateUtil.isEmpty("value", queryLike)&&!this.equalsIn(new LikeMode[] { LikeMode.IsNull, LikeMode.IsNotNull },queryLike.getLikeMode())) {
//                continue;
//            }

            if (queryLike == null || StringUtils.isBlank(queryLike.getColumnName()) || StringUtils.isBlank(queryLike.getValue()) && !this.equalsIn(new QueryLike.LikeMode[]{QueryLike.LikeMode.IsNull, QueryLike.LikeMode.IsNotNull}, queryLike.getLikeMode())) {
                continue;
            }

//            isnull = false;
            // 根据不同比较方式，生成运算符
            if (this.equalsIn(new QueryLike.LikeMode[]{QueryLike.LikeMode.Eq, QueryLike.LikeMode.NotEq, QueryLike.LikeMode.Ne, QueryLike.LikeMode.Gt, QueryLike.LikeMode.Ge, QueryLike.LikeMode.Lt, QueryLike.LikeMode.Le}, queryLike.getLikeMode())) {
                whereSql.append(" and " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue() + " ");
                whereSql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue() : "'" + queryLike.getValue() + "'");

            } else if (QueryLike.LikeMode.Like.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" and " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue());
                whereSql.append(" '%" + queryLike.getValue() + "%'");

            } else if (QueryLike.LikeMode.LikeSta.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" and " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue());
                whereSql.append(" '%" + queryLike.getValue() + "'");

            } else if (QueryLike.LikeMode.LikeEnd.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue());
                whereSql.append(" '" + queryLike.getValue() + "%'");

            } else if (QueryLike.LikeMode.Between.getCode().equals(queryLike.getLikeMode().getCode())) {

//                if (!ValidateUtil.isEmpty("value", queryLike)) {
                if (queryLike != null && !StringUtils.isBlank(queryLike.getValue())) {
                    whereSql.append(" and " + queryLike.getColumnName() + " " + QueryLike.LikeMode.Ge.getValue() + " ");
                    whereSql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue() : "'" + queryLike.getValue() + "'");
                }

//                if (!ValidateUtil.isEmpty("value2", queryLike)) {
                if (queryLike != null && !StringUtils.isBlank(queryLike.getValue2())) {
                    whereSql.append(" and " + queryLike.getColumnName() + " " + QueryLike.LikeMode.Le.getValue() + " ");
                    whereSql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue2() : "'" + queryLike.getValue2() + "'");
                }

            } else if (this.equalsIn(new QueryLike.LikeMode[]{QueryLike.LikeMode.IsNull, QueryLike.LikeMode.IsNotNull}, queryLike.getLikeMode())) {
                whereSql.append(" and " + queryLike.getLikeMode().getValue() + " (" + queryLike.getColumnName() + ")");

            } else if (QueryLike.LikeMode.Custom.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" and " + queryLike.getValue());
            }

//            whereSql.append(" ");
//            whereSql.append(" " + queryLike.getOperator());
        }
        return whereSql.toString();
//        return isnull ? "" : whereSql.toString().substring(0, whereSql.toString().lastIndexOf(" "));
    }

    private String generateGroupBySql(ShardingSpecification<T> specification) {
//        return ValidateUtil.isEmpty(criteriaDto.getGroupByName()) ? "" : " group by " + criteriaDto.getGroupByName() + " ";
        return (specification == null || StringUtils.isBlank(specification.getGroupByName())) ? "" : " group by " + specification.getGroupByName() + " ";
    }

    /**
     * 生成分页 、 排序Sql
     */
    private String generateLimitOrderSql(PageAndSort pageAndSort) {
        if (null == pageAndSort) {
            return "";
        }
        StringBuffer limitOrderSql = new StringBuffer();
        if (null != pageAndSort.getSortName() && !StringUtils.isBlank(pageAndSort.getSortName())) {
            limitOrderSql.append(" order by " + pageAndSort.getSortName() + " " + pageAndSort.getSortOrder());
        }
        if (null == pageAndSort.getPage() || null == pageAndSort.getRp() || null == pageAndSort.getRowCount()) {
            throw new RuntimeException("缺少参数：第几页、每页显示数、总记录数");
        }
        limitOrderSql.append(" limit " + pageAndSort.getStartIndex() + ", " + pageAndSort.getRp());
        return limitOrderSql.toString();
    }

    /**
     * 是否数字
     */
    private boolean isNumber(QueryLike.ColumnType columnType) {
        for (QueryLike.ColumnType temp : QueryLike.ColumnType.getNumberColumnTypes()) {
            if (temp.getValue().equals(columnType.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 包含比较
     */
    private boolean equalsIn(QueryLike.LikeMode[] sourceLikeMode, QueryLike.LikeMode targetLikeMode) {
        for (QueryLike.LikeMode likeMode : sourceLikeMode) {
            if (likeMode.getCode().equals(targetLikeMode.getCode())) {
                return true;
            }
        }
        return false;
    }
}
