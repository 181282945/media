package com.aisino.base.invoice.authcodeinfo.controller;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-5-8.
 */
@RestController
@RequestMapping(value = AclAuthCodeInfoController.PATH)
@AclResc(id = 70000,code = "authCodeInfo", name = AclAuthCodeInfoController.MODULE_NAME,homePage = AclAuthCodeInfoController.HOME_PAGE,target = AclResource.Target.ACLUSER)
public class AclAuthCodeInfoController extends BaseController<AuthCodeInfo>{
    final static String PATH = "/base/invoice/authcodeinfo/a";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "平台授权信息管理";

    @Resource
    private AuthCodeInfoService authCodeInfoService;


    //页面模板路径
    private static final String VIEW_NAME = "/list_authcodeinfo";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/invalid";
    //查询
    private static final String SEARCH_URL = PATH + "/list";


    @RequestMapping(value = "/tolist",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = generalMav(PATH,MODULE_NAME,VIEW_NAME,UPDATE_URL,ADD_URL,DELETE_URL,SEARCH_URL);
        return mav;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(id = 70001,code = "list",name = "菜单列表")
    @Transactional(readOnly = true)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AuthCodeInfo> authCodeInfos = authCodeInfoService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(authCodeInfos,pageAndSort);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 70002,code = "add",name = "新增菜单")
    public ResultDataDto add(@ModelAttribute("authCodeInfo")AuthCodeInfo authCodeInfo){
        if(authCodeInfoService.addEntity(authCodeInfo)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 70003,code = "update",name = "更新菜单")
    public ResultDataDto update(@ModelAttribute("authCodeInfo")AuthCodeInfo authCodeInfo){
        authCodeInfoService.updateEntity(authCodeInfo);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 70004,code = "delete",name = "删除菜单")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        authCodeInfoService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }
}
