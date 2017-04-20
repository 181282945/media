package com.sunjung.common.params;

import com.sunjung.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.sunjung.base.sysmgr.aclmenu.service.AclMenuService;
import com.sunjung.common.dto.param.ParamDto;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/17.
 */
@Component("params")
public class Params {
    private static ParamDto [] aclMenuParams ;

    @Resource
    private AclMenuService aclMenuService;


    @PostConstruct
    private void initAclMenuParams(){
        List<AclMenu> aclMenus = aclMenuService.findParams();
        aclMenuParams = new ParamDto[aclMenus.size()];
        for(int i = 0;i<aclMenuParams.length;i++){
            aclMenuParams[i] = new ParamDto(aclMenus.get(i).getId().toString(),aclMenus.get(i).getName());
        }
    }


    public static ParamDto [] getAclMenuParams() {
        return aclMenuParams;
    }



}
