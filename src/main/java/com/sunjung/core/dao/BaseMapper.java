package com.sunjung.core.dao;

import com.sunjung.core.dao.impl.BaseProvider;
import com.sunjung.core.entity.BaseBusinessEntity;
import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.mybatis.specification.Specification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public interface BaseMapper<T extends BaseEntity> {

    /**
     * 根据Id查询实体
     */
    @SelectProvider(type = BaseProvider.class, method = "findEntityById")
    T findEntityById(Specification<T> specification);

    /**
     * 新增实体
     */
    @InsertProvider(type = BaseProvider.class, method = "addEntity")
    @Options(useGeneratedKeys = true)
    void addEntity(T entity);
    /**
     * 更新实体
     */
    @UpdateProvider(type = BaseProvider.class, method = "updateEntity")
    void updateEntity(T entity);

    /**
     * 更新状态
     */
    @UpdateProvider(type = BaseProvider.class, method = "updateEntityStatus")
    <S extends BaseBusinessEntity> void updateEntityStatus(S entity);

    /**
     * 根据Id删除实体
     */
    @DeleteProvider(type = BaseProvider.class, method = "deleteById")
    void deleteById(Specification<T> specification);

    /**
     * 查询所有数据
     */
    @SelectProvider(type = BaseProvider.class, method = "findAll")
    List<T> findAll(Specification<T> specification);

    /**
     * 单表模糊查询，暂不支持链接查询
     */
    @SelectProvider(type = BaseProvider.class, method = "findByLike")
    List<T> findByLike(Specification<T> specification);

    /**
     * 单表模糊分页查询，暂不支持链接查询
     */
    @SelectProvider(type = BaseProvider.class, method = "findByPage")
    List<T> findByPage(Specification<T> specification);

    /**
     * 单表模糊查询总记录数
     */
    @SelectProvider(type = BaseProvider.class, method = "findRowCount")
    long findRowCount(Specification<T> specification);


    /**
     * 查找上一条
     */
    @SelectProvider(type = BaseProvider.class, method = "getEntityPreviousById")
    T getEntityPreviousById(Specification<T> specification);

    /**
     * 查找下一条
     */
    @SelectProvider(type = BaseProvider.class, method = "getEntityNextById")
    T getEntityNextById(Specification<T> specification);
}
