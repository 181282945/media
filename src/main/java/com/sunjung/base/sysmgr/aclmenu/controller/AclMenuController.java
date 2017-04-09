package com.sunjung.base.sysmgr.aclmenu.controller;

import com.sunjung.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.sunjung.base.sysmgr.aclmenu.service.AclMenuService;
import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-8.
 */
@RestController
@RequestMapping(value = {AclMenuController.MAPPING})
@AclResc(code = "aclmenu",name = "菜单管理",homePage = AclMenuController.HOME_PAGE)
public class AclMenuController extends BaseController<AclMenu> {

    final static String MAPPING="/base/sysmgr/aclmenu";

    final static String HOME_PAGE = MAPPING + "/list";

    @Resource
    private AclMenuService aclMenuService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(code = "list",name = "菜单列表")
    public void list(){

    }

    @RequestMapping(value = "/getaclusermenus",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "getAclUserMenus",name = "获取菜单")
    public ResultDataDto getAclUserMenus(){
        return ResultDataDto.addSuccess().setDatas(aclMenuService.getAclUserMenus());
    }

}
