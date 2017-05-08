package com.aisino.base.sysmgr.aclrole.service;

import com.aisino.common.dto.param.ParamDto;
import com.aisino.core.service.BaseService;
import com.aisino.base.sysmgr.aclrole.dao.AclRoleMapper;
import com.aisino.base.sysmgr.aclrole.entity.AclRole;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclRoleService extends BaseService<AclRole,AclRoleMapper> {
    /**
     * 获取前台用户角色的下拉列表格式
     */
    ParamDto[] getUserInfoRoleParams();
}
