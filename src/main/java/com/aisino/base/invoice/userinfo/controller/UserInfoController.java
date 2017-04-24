package com.aisino.base.invoice.userinfo.controller;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
@RestController
@RequestMapping(value = UserInfoController.PATH)
@AclResc(code = "userInfo", name = UserInfoController.MODULE_NAME,homePage = UserInfoController.PATH + UserInfoController.HOME_PAGE)
public class UserInfoController extends BaseController<UserInfo> {
    final static String PATH = "/base/invoice/userinfo";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "用户管理";

    @Resource
    private UserInfoService userInfoService;



    //页面模板路径
    private static final String VIEW_NAME = "/list_userinfo";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/invalid";
    //查询
    private static final String SEARCH_URL = PATH + "/list";


    @RequestMapping(value = UserInfoController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
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
    @AclResc(code = "list",name = "用户列表")
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<UserInfo> userInfos = userInfoService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(userInfos,pageAndSort);
    }


    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增用户")
    public ResultDataDto add(@ModelAttribute("userInfo")UserInfo userInfo){
        if(userInfoService.addEntity(userInfo)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新用户")
    public ResultDataDto update(@ModelAttribute("userInfo")UserInfo userInfo){
        userInfoService.updateEntity(userInfo);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 生效
     */
    @RequestMapping(value = "/effective",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "effective",name = "用户生效")
    public ResultDataDto effective(@RequestParam("id") Integer id){
        userInfoService.updateEntityEffective(id);
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 失效
     */
    @RequestMapping(value = "/invalid",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "invalid",name = "用户生效")
    public ResultDataDto invalid(@RequestParam("id") Integer id){
        userInfoService.updateEntityInvalid(id);
        return ResultDataDto.addOperationSuccess();
    }


}
