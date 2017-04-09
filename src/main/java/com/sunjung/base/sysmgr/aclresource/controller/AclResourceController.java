package com.sunjung.base.sysmgr.aclresource.controller;

import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 为 on 2017-4-8.
 */
@RestController
@RequestMapping(value = {"/base/sysmgr/aclresource"})
@AclResc(code = "aclResource", name = "资源权限",homePage = AclResourceController.HOME_PAGE)
public class AclResourceController extends BaseController<AclResource> {
    final static String HOME_PAGE = "/base/sysmgr/aclresource/list";

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(code = "list",name = "测试Thymeleaf")
    public void list(){

    }

}
