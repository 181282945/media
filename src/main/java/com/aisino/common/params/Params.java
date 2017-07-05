package com.aisino.common.params;

import com.aisino.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.aisino.base.sysmgr.aclmenu.service.AclMenuService;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/17.
 */
@Component("params")
public class Params {
    private static Parameter[] aclMenuParams ;

    @Resource
    private AclMenuService aclMenuService;


    @PostConstruct
    private void initAclMenuParams(){
        List<AclMenu> aclMenus = aclMenuService.findParams();
        aclMenuParams = new Parameter[aclMenus.size()];
        for(int i = 0;i<aclMenuParams.length;i++){
            aclMenuParams[i] = new Parameter(aclMenus.get(i).getId().toString(),aclMenus.get(i).getName());
        }
    }


    public static Parameter [] getAclMenuParams() {
        return aclMenuParams;
    }



}
