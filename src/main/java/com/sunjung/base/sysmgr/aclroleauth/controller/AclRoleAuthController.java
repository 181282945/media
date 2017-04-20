package com.sunjung.base.sysmgr.aclroleauth.controller;

import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclroleauth.entity.AclRoleAuth;
import com.sunjung.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-20.
 */
@RestController
@RequestMapping(value = AclRoleAuthController.PATH)
@AclResc(code = "aclroleauth", name = AclRoleAuthController.MODULE_NAME,homePage = AclRoleAuthController.PATH + AclRoleAuthController.HOME_PAGE)
public class AclRoleAuthController extends BaseController<AclRoleAuth> {
    final static String PATH = "/base/sysmgr/aclroleauth";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "角色权限管理";

    @Resource
    private AclRoleAuthService aclRoleAuthService;


    //页面模板路径
    private static final String VIEW_NAME = "/list_aclroleauth";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/delete";
    //查询
    private static final String SEARCH_URL = PATH + "/list";

    /**
     * 新增
     * @param aclRoleAuth
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增角色权限")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto add(@ModelAttribute("aclRoleAuth")AclRoleAuth aclRoleAuth){
        if(aclRoleAuthService.addEntity(aclRoleAuth)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 根据资源角色新增
     */
    @RequestMapping(value = "/addByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "addByRescIdRoleId",name = "新增角色权限")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto addByRescIdRoleId(@ModelAttribute("rescId")Integer rescId,@ModelAttribute("roleId")Integer roleId){
        if(aclRoleAuthService.addByRescIdRoleId(rescId, roleId)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }



    /**
     * 根据资源角色删除
     */
    @RequestMapping(value = "/deleteByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "deleteByRescIdRoleId",name = "根据资源角色新增")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto deleteByRescIdRoleId(@ModelAttribute("rescId")Integer rescId,@ModelAttribute("roleId")Integer roleId){
        aclRoleAuthService.deleteByRescIdRoleId(rescId,roleId);
        return ResultDataDto.addDeleteSuccess();
    }
}
