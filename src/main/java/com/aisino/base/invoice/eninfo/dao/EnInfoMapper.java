package com.aisino.base.invoice.eninfo.dao;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
@Mapper
public interface EnInfoMapper extends BaseMapper<EnInfo> {

    @Select("SELECT taxno FROM invoice_eninfo")
    List<String> findAllTaxno();

    @Select("SELECT * FROM invoice_eninfo where taxno = #{taxno} LIMIT 1")
    EnInfo getEnInfoByTaxno(@Param("taxno")String taxno);

}
