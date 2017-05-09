package com.aisino.base.sysmgr.aclmenu.controller;

import com.aisino.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.aisino.base.sysmgr.aclmenu.service.AclMenuService;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
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
@AclResc(id = 3000,code = "aclmenu", name = AclMenuController.MODULE_NAME,homePage = AclMenuController.HOME_PAGE,target = AclResource.Target.ACLUSER)
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
    @RequestMapping(value = "/tolist",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = generalMav(PATH,MODULE_NAME,VIEW_NAME,UPDATE_URL,ADD_URL,DELETE_URL,SEARCH_URL);
        return mav;
    }


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(id = 3001,code = "list",name = "菜单列表")
    @Transactional(readOnly = true)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclMenu> aclMenus = aclMenuService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclMenus,pageAndSort);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3002,code = "add",name = "新增菜单")
    public ResultDataDto add(@ModelAttribute("aclMenu")AclMenu aclMenu){
        if(aclMenuService.addEntity(aclMenu)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3003,code = "update",name = "更新菜单")
    public ResultDataDto update(@ModelAttribute("aclMenu")AclMenu aclMenu){
        aclMenuService.updateEntity(aclMenu);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3004,code = "delete",name = "删除菜单")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclMenuService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }

    @RequestMapping(value = "/getaclusermenus",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3005,code = "getAclUserMenus",name = "获取菜单")
    @Transactional(readOnly = true)
    public ResultDataDto getAclUserMenus(){
        return ResultDataDto.addSuccess().setDatas(aclMenuService.getAclUserMenus());
    }

}
