package com.sunjung.base.sysmgr.aclauth.service;

import com.sunjung.base.sysmgr.aclauth.dao.AclAuthMapper;
import com.sunjung.base.sysmgr.aclauth.entity.AclAuth;
import com.sunjung.core.service.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclAuthService extends BaseService<AclAuth,AclAuthMapper> {

    List<String> findCodeByRoleId(Integer roleId);

    List<String> findCodeByUserId(Integer userId);

    List<Map<String,String>> findPathCode();

    /**
     * 根据资源ID更新code
     * @param code
     * @param updateCodeByRescId
     * @return
     */
    int updateCodeByRescId(String code,Integer updateCodeByRescId);


    /**
     * 根据资源ID删除权限限制
     * @param resourceId
     * @return
     */
    int deleteByRescId(Integer resourceId);

    /**
     * 存在返回1,否则返回0
     * @param resourceId
     * @return
     */
    int existByRescId(Integer resourceId);

    /**
     * 根据资源ID查询
     * @param rescId
     * @return
     */
    AclAuth getByRescId(@Param("rescId")Integer rescId);

}
