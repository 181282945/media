package com.aisino.base.invoice.authcodeinfo.dao;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 为 on 2017-5-8.
 */
@Mapper
public interface AuthCodeInfoMapper extends BaseMapper<AuthCodeInfo> {

    @Select("SELECT taxno FROM invoice_authcodeinfo")
    List<String> findAllTaxno();

    @Select("SELECT * FROM invoice_authcodeinfo where taxNo = #{taxNo} LIMIT 1")
    AuthCodeInfo getAuthCodeInfoByTaxNo(@Param("taxNo")String taxNo);
}
