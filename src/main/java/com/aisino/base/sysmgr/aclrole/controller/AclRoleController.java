package com.aisino.base.sysmgr.aclrole.controller;

import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.base.sysmgr.aclrole.entity.AclRole;
import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-19.
 */
@RestController
@RequestMapping(value = AclRoleController.PATH)
@AclResc(id = 6000,code = "aclRole", name = AclRoleController.MODULE_NAME,homePage = AclRoleController.HOME_PAGE,target = AclResource.Target.ACLUSER)
public class AclRoleController extends BaseController<AclRole> {
    final static String PATH = "/base/sysmgr/aclrole";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "角色管理";

    @Resource
    private AclRoleService aclRoleService;

    //页面模板路径
    private static final String VIEW_NAME = "/list_aclrole";
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
        mav.addObject("aclResrouceTypeParams", ParamUtil.JqgridSelectVal(AclResource.Type.getParams()));
        mav.addObject("aclResrouceTargetParams", ParamUtil.JqgridSelectVal(AclResource.Target.getParams()));
        mav.addObject("aclRoleTargetParams", ParamUtil.JqgridSelectVal(AclRole.Target.getParams()));
        return mav;
    }


    /**
     * 查询方法
     * @param jqgridFilters
     * @param pageAndSort
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 6001,code = "list",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModule(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclRole> aclRoles = aclRoleService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclRoles,pageAndSort);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 6003,code = "add",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModule(@ModelAttribute("aclRole")AclRole aclRole){
        aclRoleService.addEntity(aclRole);
        return ResultDataDto.addAddSuccess();
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 6004,code = "update",name = "修改角色")
    public ResultDataDto update(@ModelAttribute("aclRole")AclRole aclRole){
        aclRoleService.updateEntity(aclRole);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 6002,code = "delete",name = "删除角色")
    public ResultDataDto delete(@ModelAttribute("id")Integer id){
        aclRoleService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }


}
