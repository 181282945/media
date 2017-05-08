package com.aisino.base.sysmgr.aclrescrole.controller;

import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.aisino.base.sysmgr.aclrescrole.service.AclRescRoleService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.MySecurityMetadataSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-20.
 */
@RestController
@RequestMapping(value = AclRescRoleController.PATH)
@AclResc(id= 2000,code = "aclRescRole", name = AclRescRoleController.MODULE_NAME,homePage = AclRescRoleController.HOME_PAGE,target = AclResource.Target.ACLUSER,isMenu = false)
public class AclRescRoleController extends BaseController<AclRescRole> {
    final static String PATH = "/base/sysmgr/aclrescrole";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "角色资源管理";

    @Resource
    private AclRescRoleService aclRescRoleService;

    @Resource
    private MySecurityMetadataSource securityMetadataSource;



    //页面模板路径
    private static final String VIEW_NAME = "/list_aclrescrole";
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
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        return mav;
    }



    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(id= 2001,code = "list",name = "角色资源列表")
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclRescRole> aclRescRoles = aclRescRoleService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclRescRoles,pageAndSort);
    }


    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 2002,code = "add",name = "新增角色资源")
    public ResultDataDto add(@ModelAttribute("aclRescRole")AclRescRole aclRescRole){
        if(aclRescRoleService.addEntity(aclRescRole)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2003,code = "update",name = "更新角色资源")
    public ResultDataDto update(@ModelAttribute("aclRescRole")AclRescRole aclRescRole){
        aclRescRoleService.updateEntity(aclRescRole);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2004,code = "delete",name = "删除角色资源")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclRescRoleService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }

    /**
     * 根据资源角色新增
     * @param aclRescRole
     * @return
     */
    @RequestMapping(value = "/addByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2005,code = "addByRescIdRoleId",name = "根据资源角色新增")
    public ResultDataDto addByRescIdRoleId(@ModelAttribute("aclRescRole")AclRescRole aclRescRole){
        if(aclRescRoleService.addEntity(aclRescRole)!=null){
            securityMetadataSource.doLoadResourceDefine();//刷新权限资源
            return ResultDataDto.addAddSuccess();
        }
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 根据资源角色新增
     * @param aclRescRole
     * @return
     */
    @RequestMapping(value = "/deleteByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2006,code = "deleteByRescIdRoleId",name = "根据资源角色删除")
    public ResultDataDto deleteByRescIdRoleId(@ModelAttribute("aclRescRole")AclRescRole aclRescRole){
        aclRescRoleService.deleteByRescIdRoleId(aclRescRole.getRoleId(),aclRescRole.getRescId());
        securityMetadataSource.doLoadResourceDefine();//刷新权限资源
        return ResultDataDto.addDeleteSuccess();
    }

}
