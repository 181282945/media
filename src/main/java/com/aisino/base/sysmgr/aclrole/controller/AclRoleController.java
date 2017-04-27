package com.aisino.base.sysmgr.aclrole.controller;

import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;
import com.aisino.base.sysmgr.aclresource.common.AclResourceType;
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
@AclResc(code = "aclRole", name = AclRoleController.MODULE_NAME,homePage = AclRoleController.PATH + AclRoleController.HOME_PAGE,target = AclResourceTarget.ACLUSER)
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
    @RequestMapping(value = AclRoleController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_MODULE_URL",SEARCH_URL);
        mav.addObject("aclResrouceTypeParams", ParamUtil.JqgridSelectVal(AclResourceType.getParams()));
        return mav;
    }


    /**
     * 查询方法
     * @param jqgridFilters
     * @param pageAndSort
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "list",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listModule(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclRole> aclRoles = aclRoleService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclRoles,pageAndSort);
    }


}
