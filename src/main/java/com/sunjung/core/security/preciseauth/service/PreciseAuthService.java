package com.sunjung.core.security.preciseauth.service;

import com.sunjung.core.security.preciseauth.dao.PreciseAuthMapper;
import com.sunjung.core.security.preciseauth.entity.PreciseAuth;
import com.sunjung.core.service.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ZhenWeiLai on 2017/4/6.
 */
public interface PreciseAuthService extends BaseService<PreciseAuth,PreciseAuthMapper> {

    /**
     * 根据角色ID查询权限
     * @param roleId
     * @return
     */
    List<String> findAuthByRoleId(String roleId);

    /**
     * 根据用户ID查询权限
     * @param userId
     * @return
     */
    List<String> findAuthByUserId(@Param("userId") String userId);

    List<Map<String,String>> findPathAuth();
}
