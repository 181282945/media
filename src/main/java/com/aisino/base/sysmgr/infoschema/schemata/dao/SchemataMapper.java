package com.aisino.base.sysmgr.infoschema.schemata.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by 为 on 2017-5-19.
 */
@Repository("schemataMapper")
public interface SchemataMapper {
    /**
     * 根据数据库名查询,存在返回1,否则返回0
     * @return
     */
    @Select("SELECT count(0) FROM  SCHEMATA WHERE SCHEMA_NAME = #{SCHEMA_NAME} ")
    int getFlagByDbName(@Param("SCHEMA_NAME") String dbName);
}
