package com.aisino.base.sysmgr.aclmenu.dao;

import com.aisino.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.aisino.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclMenuMapper extends BaseMapper<AclMenu> {

    /**
     * 获取CODE NAME 作为选择参数
     * @return
     */
    @Select("SELECT id,name FROM acl_menu")
    List<AclMenu> findParams();

}
