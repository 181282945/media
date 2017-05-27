package com.aisino.base.sysmgr.infoschema.schemata.service.impl;

import com.aisino.base.sysmgr.infoschema.schemata.dao.SchemataMapper;
import com.aisino.base.sysmgr.infoschema.schemata.service.SchemataService;
import com.aisino.core.mybatis.DataSourceContextHolder;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-5-19.
 */
@Service("schemataService")
public class SchemataServiceImpl implements SchemataService {

    @Resource(name="sqlSessionTemplate")
    private SqlSession sqlSession;


    private SchemataMapper getMapper() {
        return sqlSession.getMapper(SchemataMapper.class);
    }


    @Override
    public boolean checkDbExist(String dbName){
        DataSourceContextHolder.info();
        int result = this.getMapper().getFlagByDbName(dbName);
        DataSourceContextHolder.write();
        if(result > 0)
            return true;
        return false;
    }

}
