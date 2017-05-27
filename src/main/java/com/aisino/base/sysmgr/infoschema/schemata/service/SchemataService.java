package com.aisino.base.sysmgr.infoschema.schemata.service;

/**
 * Created by 为 on 2017-5-19.
 */
public interface SchemataService {

    /**
     *  检查数据库是否存在
     * @param dbName
     * @return
     */
    boolean checkDbExist(String dbName);
}
