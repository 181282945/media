package com.sunjung.base.sysmgr.aclrescrole.controller;

import com.sunjung.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.sunjung.base.sysmgr.aclrescrole.service.AclRescRoleService;
import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-20.
 */
@RestController
@RequestMapping(value = AclRescRoleController.PATH)
@AclResc(code = "aclRescRole", name = AclRescRoleController.MODULE_NAME,homePage = AclRescRoleController.PATH + AclRescRoleController.HOME_PAGE)
public class AclRescRoleController extends BaseController<AclRescRole> {
    final static String PATH = "/base/sysmgr/aclrescrole";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "角色资源管理";

    @Resource
    private AclRescRoleService aclRescRoleService;



    //页面模板路径
    private static final String VIEW_NAME = "/list_aclrescrole";
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
    @RequestMapping(value = AclRescRoleController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_MODULE_URL",SEARCH_MODULE_URL);
        mav.addObject("SEARCH_METHOD_URL",SEARCH_METHOD_URL);
        return mav;
    }

    /**
     * 根据资源角色新增
     * @param aclRescRole
     * @return
     */
    @RequestMapping(value = "/addByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "addByRescIdRoleId",name = "根据资源角色新增")
    public ResultDataDto addByRescIdRoleId(@ModelAttribute("aclRescRole")AclRescRole aclRescRole){
        if(aclRescRoleService.addEntity(aclRescRole)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 根据资源角色新增
     * @param aclRescRole
     * @return
     */
    @RequestMapping(value = "/deleteByRescIdRoleId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "deleteByRescIdRoleId",name = "根据资源角色新增")
    public ResultDataDto deleteByRescIdRoleId(@ModelAttribute("aclRescRole")AclRescRole aclRescRole){
        aclRescRoleService.deleteByRescIdRoleId(aclRescRole.getRoleId(),aclRescRole.getRescId());
        return ResultDataDto.addDeleteSuccess();
    }

}
