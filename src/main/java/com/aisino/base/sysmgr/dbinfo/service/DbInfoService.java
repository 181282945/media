package com.aisino.base.sysmgr.dbinfo.service;

import com.aisino.base.sysmgr.dbinfo.dao.DbInfoMapper;
import com.aisino.base.sysmgr.dbinfo.entity.DbInfo;
import com.aisino.core.service.BaseService;

/**
 * Created by 为 on 2017-4-27.
 */
public interface DbInfoService extends BaseService<DbInfo,DbInfoMapper> {

    /**
     * 根据企业税号创建数据库
     */
    Integer addByTaxNo(String taxNo);


    /**
     * 根据税号获取数据库信息
     */
    DbInfo getDbInfoByTaxNo(String taxNo);
}
