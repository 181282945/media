package com.aisino.base.sysmgr.acluserauth.controller;

import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.acluserauth.entity.AclUserAuth;
import com.aisino.base.sysmgr.acluserauth.service.AclUserAuthService;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-24.
 */
@RestController
@RequestMapping(value = AclUserAuthController.PATH)
public class AclUserAuthController extends BaseController<AclUserAuth> {
    final static String PATH = "/base/sysmgr/acluserauth";

    @Resource
    private AclUserAuthService aclUserAuthService;


    /**
     * 根据资源角色新增
     */
    @RequestMapping(value = "/addByRescIdUserId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "addByRescIdUserId",name = "新增用户权限")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto addByRescIdRoleId(@ModelAttribute("rescId")Integer rescId,@ModelAttribute("userId")Integer userId){
        if(aclUserAuthService.addByRescIdUserId(rescId, userId)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }



    /**
     * 根据资源角色删除
     */
    @RequestMapping(value = "/deleteByRescIdUserId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "deleteByRescIdUserId",name = "根据资源角色新增")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto deleteByRescIdUserId(@ModelAttribute("rescId")Integer rescId,@ModelAttribute("userId")Integer userId){
        aclUserAuthService.deleteByAuthIdUserId(rescId,userId);
        return ResultDataDto.addDeleteSuccess();
    }

}
