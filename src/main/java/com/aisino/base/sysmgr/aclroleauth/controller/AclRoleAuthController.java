package com.aisino.base.sysmgr.aclroleauth.controller;

import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclroleauth.entity.AclRoleAuth;
import com.aisino.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
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
@AclResc(code = "aclroleauth", name = AclRoleAuthController.MODULE_NAME,homePage = AclRoleAuthController.PATH + AclRoleAuthController.HOME_PAGE,target = AclResourceTarget.ACLUSER)
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
     * @return
     */
    @RequestMapping(value = AclRoleAuthController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        return mav;
    }


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(code = "list",name = "角色权限列表")
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
    @AclResc(code = "add",name = "新增角色权限")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto add(@ModelAttribute("aclRoleAuth")AclRoleAuth aclRoleAuth){
        if(aclRoleAuthService.addEntity(aclRoleAuth)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }





    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新角色权限")
    public ResultDataDto update(@ModelAttribute("aclRoleAuth")AclRoleAuth aclRoleAuth){
        aclRoleAuthService.updateEntity(aclRoleAuth);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "delete",name = "删除角色资源")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclRoleAuthService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
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
