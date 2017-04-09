package com.sunjung.base.sysmgr.aclauth.service;

import com.sunjung.base.sysmgr.aclauth.dao.AclAuthMapper;
import com.sunjung.base.sysmgr.aclauth.entity.AclAuth;
import com.sunjung.core.service.BaseService;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclAuthService extends BaseService<AclAuth,AclAuthMapper> {

    List<String> findCodeByRoleId(Integer roleId);

    List<String> findCodeByUserId(Integer userId);

    List<Map<String,String>> findPathCode();

}
