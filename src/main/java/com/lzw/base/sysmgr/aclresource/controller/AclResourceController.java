package com.lzw.base.sysmgr.aclresource.controller;

import com.lzw.base.sysmgr.aclresource.entity.AclResource;
import com.lzw.base.sysmgr.aclresource.service.AclResourceService;
import com.lzw.core.mybatis.specification.PageAndSort;
import com.lzw.base.sysmgr.aclresource.annotation.AclResc;
import com.lzw.common.dto.jqgrid.JqgridFilters;
import com.lzw.common.params.Params;
import com.lzw.common.util.ParamUtil;
import com.lzw.core.controller.BaseController;
import com.lzw.core.dto.ResultDataDto;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@RestController
@RequestMapping(value = AclResourceController.PATH)
@AclResc(id= 1000,code = "aclResource", name = AclResourceController.MODULE_NAME,homePage = AclResourceController.HOME_PAGE)
public class AclResourceController extends BaseController<AclResource> {
    final static String PATH = "/base/sysmgr/aclresource";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "资源管理";

    @Resource
    private AclResourceService aclResourceService;

    //页面模板路径
    private static final String VIEW_NAME = "/list_aclresource";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/delete";
    //查询 模块
    private static final String SEARCH_MODULE_URL = PATH + "/listModule";
    //查询 方法
    private static final String SEARCH_METHOD_URL = PATH + "/listMethod";

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
        mav.addObject("SEARCH_MODULE_URL",SEARCH_MODULE_URL);
        mav.addObject("SEARCH_METHOD_URL",SEARCH_METHOD_URL);
        mav.addObject("aclResrouceTypeParams",ParamUtil.JqgridSelectVal(AclResource.Type.getParams()));
        mav.addObject("aclMenuParams",ParamUtil.JqgridSelectVal(Params.getAclMenuParams()));
        return mav;
    }

    /**
     * 模块查询列表
     */
    @RequestMapping(value = "/listModule",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1001,code = "listModule",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModule(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclResource> aclResources = aclResourceService.findModuleByFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclResources,pageAndSort);
    }

    /**
     * 根据后台用户ID查询,返回该用户是否拥有权限
     */
    @RequestMapping(value = "/listModuleAuthByAclUserId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1002,code = "listModuleAuthByAclUserId",name = "后台用户模块权限查询")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModuleAuthByAclUserId(@RequestParam("aclUserId")Integer aclUserId){
        List<AclResource> aclResources = aclResourceService.findAllModule();
        aclResourceService.fillIsAuthByRescUser(aclResources,aclUserId);
        return new ResultDataDto(aclResources);
    }

    /**
     * 根据前台用户ID查询,返回该用户是否拥有权限
     */
    @RequestMapping(value = "/listModuleAuthByUserId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1003,code = "listModuleAuthByUserId",name = "后台用户模块权限查询")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModuleAuthByUserId(@RequestParam("userId")Integer userId){
        List<AclResource> aclResources = aclResourceService.findAllUserModule();
        aclResourceService.fillIsAuthByRescUser(aclResources,userId);
        return new ResultDataDto(aclResources);
    }




    /**
     * 根据角色ID查询,返回该角色是否拥有权限
     */
    @RequestMapping(value = "/listModuleAuthByRoleId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1004,code = "listModuleAuthByRoleId",name = "角色模块权限查询")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModuleAuthByRoleId(@RequestParam("roleId")Integer roleId){
        List<AclResource> aclResources = aclResourceService.findAllModule();
        aclResourceService.fillIsAuthByRescRole(aclResources,roleId);
        return new ResultDataDto(aclResources);
    }

    /**
     * 查询方法
     */
    @RequestMapping(value = "/listMethod",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1005,code = "listMethod",name = "方法列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listMethod(@RequestParam("moduleId") Integer moduleId){
        List<AclResource> aclResources = aclResourceService.findMethodByModuleId(moduleId);
        aclResourceService.fillIsAuth(aclResources);
        return new ResultDataDto(aclResources);
    }

    /**
     * 查询方法
     */
    @RequestMapping(value = "/listMethodAuth",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1006,code = "listMethodAuth",name = "精确权限")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listMethodAuth(@RequestParam("moduleId") Integer moduleId,@ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclResource> aclResources = aclResourceService.findMethodAuth(moduleId,pageAndSort);
        return new ResultDataDto(aclResources,pageAndSort);
    }

    /**
     * 查询方法
     */
    @RequestMapping(value = "/listMethodAuthByRoleId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1007,code = "listMethodAuthByRoleId",name = "角色方法权限查询")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listMethodAuthByRoleId(@RequestParam("moduleId") Integer moduleId,@RequestParam("roleId") Integer roleId,@ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclResource> aclResources = aclResourceService.findMethodAuth(moduleId,pageAndSort);
        aclResourceService.fillIsAuthByRoleAuth(aclResources,roleId);
        return new ResultDataDto(aclResources,pageAndSort);
    }

    /**
     * 查询方法
     */
    @RequestMapping(value = "/listMethodAuthByUserId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1008,code = "listMethodAuthByUserId",name = "用户方法权限查询")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listMethodAuthByUserId(@RequestParam("moduleId") Integer moduleId,@RequestParam("userId") Integer userId,@ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclResource> aclResources = aclResourceService.findMethodAuth(moduleId,pageAndSort);
        aclResourceService.fillIsAuthByRoleAuth(aclResources,userId);
        return new ResultDataDto(aclResources,pageAndSort);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1009,code = "add",name = "新增资源")
    public ResultDataDto add(@ModelAttribute("aclResource")AclResource aclResource){
         if(aclResourceService.addEntity(aclResource)!=null)
             return ResultDataDto.addAddSuccess();
         return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     */
    @RequestMapping(value = "/view",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 10010,code = "view",name = "明细")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto view(@RequestParam("id")Integer id){
        return new ResultDataDto(aclResourceService.findEntityById(id));
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id= 1011,code = "update",name = "更新资源")
    public ResultDataDto update(@ModelAttribute("aclResource")AclResource aclResource){
        aclResourceService.updateEntity(aclResource);
        return ResultDataDto.addUpdateSuccess();
    }
}
