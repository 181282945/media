package com.sunjung.base.sysmgr.aclresource.controller;

import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.common.dto.jqgrid.JqgridFilters;
import com.sunjung.common.params.Params;
import com.sunjung.common.util.ParamUtil;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import com.sunjung.core.mybatis.specification.PageAndSort;
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
@AclResc(code = "aclResource", name = AclResourceController.MODULE_NAME,homePage = AclResourceController.PATH + AclResourceController.HOME_PAGE)
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
    @RequestMapping(value = AclResourceController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_MODULE_URL",SEARCH_MODULE_URL);
        mav.addObject("SEARCH_METHOD_URL",SEARCH_METHOD_URL);
        mav.addObject("aclResrouceTypeParams",ParamUtil.JqgridSelectVal(AclResourceType.getParams()));
        mav.addObject("aclMenuParams",ParamUtil.JqgridSelectVal(Params.getAclMenuParams()));
        return mav;
    }

    /**
     * 模块查询列表
     * @param jqgridFilters
     * @param pageAndSort
     * @return
     */
    @RequestMapping(value = "/listModule",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "listModule",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModule(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclResource> aclResources = aclResourceService.findModuleByFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclResources,pageAndSort);
    }

    /**
     * 根据角色ID查询,返回该角色是否拥有权限
     * @param roleId
     * @param
     * @return
     */
    @RequestMapping(value = "/listModuleAuthByRoleId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "listModuleAuthByRoleId",name = "角色模块权限查询")
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
    @AclResc(code = "listMethod",name = "方法列表")
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
    @AclResc(code = "listMethodAuth",name = "精确权限")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listMethodAuth(@RequestParam("moduleId") Integer moduleId){
        List<AclResource> aclResources = aclResourceService.findMethodAuth(moduleId);
        return new ResultDataDto(aclResources);
    }

    /**
     * 查询方法
     */
    @RequestMapping(value = "/listMethodAuthByRoleId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "listMethodAuthByRoleId",name = "角色方法权限查询")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listMethodAuthByRoleId(@RequestParam("moduleId") Integer moduleId,@RequestParam("roleId") Integer roleId){
        List<AclResource> aclResources = aclResourceService.findMethodAuth(moduleId);
        aclResourceService.fillIsAuthByRoleAuth(aclResources,roleId);
        return new ResultDataDto(aclResources);
    }

    /**
     * 新增
     * @param aclResource
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增资源")
    public ResultDataDto add(@ModelAttribute("aclResource")AclResource aclResource){
         if(aclResourceService.addEntity(aclResource)!=null)
             return ResultDataDto.addAddSuccess();
         return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     * @return
     */
    @RequestMapping(value = "/view",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "view",name = "明细")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto view(@RequestParam("id")Integer id){
        return new ResultDataDto(aclResourceService.findEntityById(id));
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "delete",name = "删除资源")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclResourceService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }

    /**
     * 更新
     * @param aclResource
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新资源")
    public ResultDataDto update(@ModelAttribute("aclResource")AclResource aclResource){
        aclResourceService.updateEntity(aclResource);
        return ResultDataDto.addUpdateSuccess();
    }
}
