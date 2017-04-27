package com.aisino.base.sysmgr.aclmenu.controller;

import com.aisino.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.aisino.base.sysmgr.aclmenu.service.AclMenuService;
import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
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
@RequestMapping(value = AclMenuController.PATH)
@AclResc(code = "aclmenu", name = AclMenuController.MODULE_NAME,homePage = AclMenuController.PATH + AclMenuController.HOME_PAGE,target = AclResourceTarget.ACLUSER)
public class AclMenuController extends BaseController<AclMenu> {


    final static String PATH = "/base/sysmgr/aclmenu";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "菜单管理";


    //页面模板路径
    private static final String VIEW_NAME = "/list_aclmenu";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/delete";
    //查询
    private static final String SEARCH_URL = PATH + "/list";

    @Resource
    private AclMenuService aclMenuService;


    /**
     * @return
     */
    @RequestMapping(value = AclMenuController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
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
    @AclResc(code = "list",name = "菜单列表")
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclMenu> aclMenus = aclMenuService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclMenus,pageAndSort);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增菜单")
    public ResultDataDto add(@ModelAttribute("aclMenu")AclMenu aclMenu){
        if(aclMenuService.addEntity(aclMenu)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新菜单")
    public ResultDataDto update(@ModelAttribute("aclMenu")AclMenu aclMenu){
        aclMenuService.updateEntity(aclMenu);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "delete",name = "删除菜单")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclMenuService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }

    @RequestMapping(value = "/getaclusermenus",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "getAclUserMenus",name = "获取菜单")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto getAclUserMenus(){
        return ResultDataDto.addSuccess().setDatas(aclMenuService.getAclUserMenus());
    }

}
