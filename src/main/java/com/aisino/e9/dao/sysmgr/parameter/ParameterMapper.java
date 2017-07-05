package com.aisino.e9.dao.sysmgr.parameter;

import com.aisino.core.dao.BaseMapper;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 为 on 2017-6-12.
 */
@Mapper
public interface ParameterMapper extends BaseMapper<Parameter> {

    /**
     * 根据类型ID查询参数
     */
    @Select("SELECT * FROM sysmgr_parameter where typeId = #{typeId}")
    List<Parameter> findByTypeId(@Param("typeId")Integer typeId);
}
