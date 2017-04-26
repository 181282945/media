package com.aisino.core.dao.impl;

import com.aisino.core.entity.BaseBusinessEntity;
import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.util.EntityColumnUtil;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.mybatis.specification.QueryLike.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 动态生成SQL
 */
public class BaseProvider<T extends BaseEntity> {

    public final static String TABLENAME_KEY = "tablename";

    /**
     * 根据Id查询实体
     */
    public String findEntityById(Specification<T> specification) {
        return "SELECT * FROM " + specification.getTableName() + " WHERE " + specification.getPrimaryKey() + " = " + specification.getEntityId();
    }

    /**
     * 根据Id查询实体
     */
    public String findEntityByNaturalId(Specification<T> specification) {
        return "SELECT * FROM " + specification.getTableName() + " WHERE " + specification.getPrimaryKey() + " = '" + specification.getEntityId() + "'";
    }

    /**
     * Athos  2016年10月14日09:39:29
     * 查找上一条
     */
    public String getEntityPreviousById(Specification<T> specification) {
        return "SELECT * FROM " + specification.getTableName() + " where id = ( select max(id) from " + specification.getTableName() + " where id < "+ specification.getEntityId()+")";

    }

    /**
     * Athos  2016年10月14日09:39:29
     * 查找下一条
     */
    public String getEntityNextById(Specification<T> specification) {
        return "SELECT * FROM " + specification.getTableName() + " where id = ( select min(id) from " + specification.getTableName() + " where id > "+ specification.getEntityId()+")";
    }

    /**
     * 根据Id查询实体
     */
    public String findAll(Specification<T> specification) {
        StringBuilder sqlString = new StringBuilder();
        sqlString.append("select * from " + specification.getTableName());
        PageAndSort pageAndSort = specification.getPageAndSort();
        if (null != pageAndSort && null != pageAndSort.getSortName() && pageAndSort.getSortName().length()>0) {
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
        if (sql.toString().lastIndexOf(" , ") == sql.toString().length()-3) {
            sql = new StringBuilder(sql.toString().substring(0, sql.toString().lastIndexOf(" , ")));
        }
        sql.append(") values (");
        index = 1;
        for (QueryLike queryLike : queryLikes) {
            if (queryLike.getIsTransient()) {
                continue;
            }
            sql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue().replaceAll("'","\\\\\'") : "'" + queryLike.getValue().replaceAll("'","\\\\\'") + "'");
            if (index < queryLikes.size()) {
                sql.append(" , ");
            }
            index++;
        }
        if (sql.toString().lastIndexOf(" , ") == sql.toString().length()-3) {
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
            sql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue().replaceAll("'","\\\\\'") : "'" + queryLike.getValue().replaceAll("'","\\\\\'") + "'");
            if (index < queryLikes.size()) {
                sql.append(" , ");
            }
            index++;
        }
        if (sql.toString().lastIndexOf(" , ") == sql.toString().length()-3) {
            sql = new StringBuilder(sql.toString().substring(0, sql.toString().lastIndexOf(" , ")));
        }

        sql.append(" where " + baseEntityMapper.primaryKey() + " = " + entity.getId());
        return sql.toString();
    }

    /**
     * 更新实体状态
     */
    public <S extends BaseBusinessEntity> String updateEntityStatus(S entity) {
        return "update " + getTableNameThrowException(entity.getClass()) + " set status = '" + entity.getStatus() + "' where id = " + entity.getId();
    }

    /**
     * 更新实体为失效
     */
    public <S extends BaseBusinessEntity> String updateEntityInvalid(S entity) {
        return "update " + getTableNameThrowException(entity.getClass()) + " set delflags = true where id = " + entity.getId();
    }

    /**
     * 更新实体为生效
     */
    public <S extends BaseBusinessEntity> String updateEntityEffective(S entity) {
        return "update " + getTableNameThrowException(entity.getClass()) + " set delflags = false where id = " + entity.getId();
    }

    /**
     * 根据Id删除实体
     */
    public String deleteById(Specification<T> specification) {
        return "delete from " + specification.getTableName() + " where id = " + specification.getEntityId();
    }

    /**
     * 单表模糊查询，暂不支持链接查询
     */
    public String findByLike(Specification<T> specification) {

        if (null == specification.getTableName()) {
            throw new RuntimeException("没有设置查询表名：tablename");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select * from " + specification.getTableName());
        sql.append(generateWhereSql(specification.getQueryLikes()));
        sql.append(generateGroupBySql(specification));
        return sql.toString();
    }

    /**
     * 单表模糊分页查询，暂不支持链接查询
     */
    public String findByPage(Specification<T> specification) {
        StringBuffer sql = new StringBuffer();
        sql.append(findByLike(specification));
        sql.append(generateLimitOrderSql(specification.getPageAndSort()));
        return sql.toString();
    }

    /**
     * 单表模糊查询，暂不支持链接查询
     */
    public String findRowCount(Specification<T> specification) {

        if (null == specification.getTableName()) {
            throw new RuntimeException("没有设置查询表名：tablename");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select count(0) from " + specification.getTableName());
        sql.append(generateWhereSql(specification.getQueryLikes()));
        sql.append(generateGroupBySql(specification));
        return sql.toString();
    }

    // ------------------------------------------- private ------------------------------------------------


    /**
     * 根据实体注解，获取表名，如果没有则抛出异常
     */
    private String getTableNameThrowException(Class<? extends BaseEntity> cls) {
        BaseEntityMapper baseEntityMapper = cls.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper || null == baseEntityMapper.tableName() || 0 == baseEntityMapper.tableName().trim().length()) {
            throw new RuntimeException("实体：" + cls.getName() + "  ,  请配置注解BaseEntityMapper");
        }
        return baseEntityMapper.tableName();
    }

    private BaseEntityMapper getBaseEntityMapperThrowException(Class<? extends BaseEntity> cls) {
        BaseEntityMapper baseEntityMapper = cls.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper || null == baseEntityMapper.tableName() || 0 == baseEntityMapper.tableName().trim().length()) {
            throw new RuntimeException("实体：" + cls.getName() + "  ,  请配置注解BaseEntityMapper");
        }
        return baseEntityMapper;
    }

    /**
     * 生成动态查询条件
     */
    private String generateWhereSql(List<QueryLike> queryLikes) {
        if (null == queryLikes || queryLikes.isEmpty()) {
            return "";
        }
        boolean isnull = true;
        StringBuffer whereSql = new StringBuffer();
        whereSql.append(" where ");
        for (QueryLike queryLike : queryLikes) {
//            if (ValidateUtil.isEmpty("columnName", queryLike) || ValidateUtil.isEmpty("value", queryLike)&&!this.equalsIn(new LikeMode[] { LikeMode.IsNull, LikeMode.IsNotNull },queryLike.getLikeMode())) {
//                continue;
//            }

            if (queryLike == null || StringUtils.isBlank(queryLike.getColumnName()) || StringUtils.isBlank(queryLike.getValue())&&!this.equalsIn(new LikeMode[] { LikeMode.IsNull, LikeMode.IsNotNull },queryLike.getLikeMode())) {
                continue;
            }

            isnull = false;
            // 根据不同比较方式，生成运算符
            if (this.equalsIn(new LikeMode[] { LikeMode.Eq, LikeMode.NotEq, LikeMode.Ne, LikeMode.Gt, LikeMode.Ge, LikeMode.Lt, LikeMode.Le }, queryLike.getLikeMode())) {
                whereSql.append(" " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue() + " ");
                whereSql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue() : "'" + queryLike.getValue() + "'");

            } else if (LikeMode.Like.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue());
                whereSql.append(" '%" + queryLike.getValue() + "%'");

            } else if (LikeMode.LikeSta.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue());
                whereSql.append(" '%" + queryLike.getValue() + "'");

            } else if (LikeMode.LikeEnd.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" " + queryLike.getColumnName() + " " + queryLike.getLikeMode().getValue());
                whereSql.append(" '" + queryLike.getValue() + "%'");

            } else if (LikeMode.Between.getCode().equals(queryLike.getLikeMode().getCode())) {

//                if (!ValidateUtil.isEmpty("value", queryLike)) {
                if (queryLike !=null && !StringUtils.isBlank(queryLike.getValue())) {
                    whereSql.append(" " + queryLike.getColumnName() + " " + LikeMode.Ge.getValue() + " ");
                    whereSql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue() : "'" + queryLike.getValue() + "'");
                }

//                if (!ValidateUtil.isEmpty("value2", queryLike)) {
                if (queryLike != null && !StringUtils.isBlank(queryLike.getValue2())) {
                    whereSql.append(" and " + queryLike.getColumnName() + " " + LikeMode.Le.getValue() + " ");
                    whereSql.append(isNumber(queryLike.getColumnType()) ? queryLike.getValue2() : "'" + queryLike.getValue2() + "'");
                }

            } else if (this.equalsIn(new LikeMode[] { LikeMode.IsNull, LikeMode.IsNotNull }, queryLike.getLikeMode())) {
                whereSql.append(" " + queryLike.getLikeMode().getValue() + " (" + queryLike.getColumnName() + ")");

            } else if (LikeMode.Custom.getCode().equals(queryLike.getLikeMode().getCode())) {
                whereSql.append(" " + queryLike.getValue());
            }

            whereSql.append(" ");
            whereSql.append(" " + queryLike.getOperator());
        }
        return isnull ? "" : whereSql.toString().substring(0, whereSql.toString().lastIndexOf(" ") );
    }

    private String generateGroupBySql(Specification<T> specification) {
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
    private boolean isNumber(ColumnType columnType) {
        for (ColumnType temp : ColumnType.getNumberColumnTypes()) {
            if (temp.getValue().equals(columnType.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 包含比较
     */
    private boolean equalsIn(LikeMode[] sourceLikeMode, LikeMode targetLikeMode) {
        for (LikeMode likeMode : sourceLikeMode) {
            if (likeMode.getCode().equals(targetLikeMode.getCode())) {
                return true;
            }
        }
        return false;
    }
}
