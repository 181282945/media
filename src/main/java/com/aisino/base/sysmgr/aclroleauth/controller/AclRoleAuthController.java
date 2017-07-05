package com.aisino.base.sysmgr.aclroleauth.controller;

import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclroleauth.entity.AclRoleAuth;
import com.aisino.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.MySecurityMetadataSource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-20.
 */
@RestController
@RequestMapping(value = AclRoleAuthController.PATH)
@AclResc(id = 700,code = "aclroleauth", name = AclRoleAuthController.MODULE_NAME,homePage = AclRoleAuthController.HOME_PAGE,target = AclResource.Target.ACLUSER,isMenu = false)
public class AclRoleAuthController extends BaseController {
    final static String PATH = "/base/sysmgr/aclroleauth";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "角色权限管理";

    @Resource
    private AclRoleAuthService aclRoleAuthService;

    @Resource
    private MySecurityMetadataSource securityMetadataSource;


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
     * @return
     */
    @RequestMapping(value = "/tolist",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = generalMav(PATH,MODULE_NAME,VIEW_NAME,UPDATE_URL,ADD_URL,DELETE_URL,SEARCH_URL);
        return mav;
    }


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(id = 701,code = "list",name = "角色权限列表")
    @Transactional(readOnly = true)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclRoleAuth> aclRoleAuths = aclRoleAuthService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclRoleAuths,pageAndSort);
    }


    /**
     * 新增
     * @param aclRoleAuth
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 702,code = "add",name = "新增角色权限")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto add(@ModelAttribute("aclRoleAuth")AclRoleAuth aclRoleAuth){
        if(aclRoleAuthService.addEntity(aclRoleAuth)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }





    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 703,code = "update",name = "更新角色权限")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto update(@ModelAttribute("aclRoleAuth")AclRoleAuth aclRoleAuth){
        aclRoleAuthService.updateEntity(aclRoleAuth);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 704,code = "delete",name = "删除角色资源")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclRoleAuthService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }




    /**
     * 根据资源角色新增
     */
    @RequestMapping(value = "/addByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 705,code = "addByRescIdRoleId",name = "新增角色权限")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto addByRescIdRoleId(@ModelAttribute("rescId")Integer rescId,@ModelAttribute("roleId")Integer roleId){
        if(aclRoleAuthService.addByRescIdRoleId(rescId, roleId)!=null){
            securityMetadataSource.doLoadResourceDefine();
            return ResultDataDto.addAddSuccess();
        }

        return ResultDataDto.addOperationFailure("保存失败!");
    }



    /**
     * 根据资源角色删除
     */
    @RequestMapping(value = "/deleteByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 706,code = "deleteByRescIdRoleId",name = "根据资源角色新增")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto deleteByRescIdRoleId(@ModelAttribute("rescId")Integer rescId,@ModelAttribute("roleId")Integer roleId){
        aclRoleAuthService.deleteByRescIdRoleId(rescId,roleId);
        securityMetadataSource.doLoadResourceDefine();
        return ResultDataDto.addDeleteSuccess();
    }
}
