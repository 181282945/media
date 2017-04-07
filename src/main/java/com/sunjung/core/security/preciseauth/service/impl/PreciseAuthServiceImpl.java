package com.sunjung.core.security.preciseauth.service.impl;

import com.sunjung.core.security.preciseauth.dao.PreciseAuthMapper;
import com.sunjung.core.security.preciseauth.entity.PreciseAuth;
import com.sunjung.core.security.preciseauth.service.PreciseAuthService;
import com.sunjung.core.service.BaseServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ZhenWeiLai on 2017/4/6.
 */
@Service
public class PreciseAuthServiceImpl extends BaseServiceImpl<PreciseAuth,PreciseAuthMapper> implements PreciseAuthService {

    @Override
    public List<String> findAuthByRoleId(String roleId){
        return getMapper().findAuthByRoleId(roleId);
    }

    @Override
    public List<String> findAuthByUserId(String userId){
        return getMapper().findAuthByUserId(userId);
    }

    @Override
    public List<Map<String,String>> findPathAuth(){
        return getMapper().findPathAuth();
    }
}
