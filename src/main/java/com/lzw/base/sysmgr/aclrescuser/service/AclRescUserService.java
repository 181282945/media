package com.lzw.base.sysmgr.aclrescuser.service;

import com.lzw.base.sysmgr.aclrescuser.dao.AclRescUserMapper;
import com.lzw.base.sysmgr.aclrescuser.entity.AclRescUser;
import com.lzw.core.service.BaseService;

import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
public interface AclRescUserService extends BaseService<AclRescUser,AclRescUserMapper> {



    List<AclRescUser> findByRescId(Integer rescId);


    /**
     * 根据角色ID,资源ID,查询是否存在权限
     * @param roleId
     * @param rescId
     * @return
     */
    int existByUserIdRescId(Integer roleId, Integer rescId);

    /**
     * 根据角色ID,资源ID 删除角色权限
     * @param userId
     * @param rescId
     * @return
     */
    int deleteByRescIdUserId(Integer userId, Integer rescId);
}
