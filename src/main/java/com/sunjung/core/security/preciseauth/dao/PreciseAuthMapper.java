package com.sunjung.core.security.preciseauth.dao;

import com.sunjung.core.dao.BaseMapper;
import com.sunjung.core.security.preciseauth.entity.PreciseAuth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ZhenWeiLai on 2017/4/6.
 */
@Repository
public interface PreciseAuthMapper extends BaseMapper<PreciseAuth> {

    @Select("SELECT auth FROM precise_auth a LEFT JOIN role_auth b ON b.pre_auth_id = a.id WHERE b.role_id = #{roleId}")
    List<String> findAuthByRoleId(@Param("roleId") String roleId);

    @Select("SELECT auth FROM precise_auth a LEFT JOIN user_auth b ON b.pre_auth_id = a.id WHERE b.user_id = #{userId}")
    List<String> findAuthByUserId(@Param("userId") String userId);

    @Select("SELECT res_string,auth FROM precise_auth a LEFT JOIN resc b ON a.rescId = b.id")
    List<Map<String,String>> findPathAuth();
}
