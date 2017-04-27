package com.aisino.base.invoice.eninfo.controller;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
@RestController
@RequestMapping(value = EnInfoController.PATH)
@AclResc(code = "eninfo", name = EnInfoController.MODULE_NAME,homePage = EnInfoController.PATH + EnInfoController.HOME_PAGE)
public class EnInfoController extends BaseController<EnInfo> {
    final static String PATH = "/base/invoice/eninfo";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "企业信息管理";

    @Resource
    private EnInfoService enInfoService;



    //页面模板路径
    private static final String VIEW_NAME = "/list_eninfo";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/invalid";
    //查询
    private static final String SEARCH_URL = PATH + "/list";


    @RequestMapping(value = EnInfoController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        return mav;
    }


    @RequestMapping(value = "/addEnInfoMav",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView addEnfo(){
        ModelAndView mav = new ModelAndView(PATH + "/add_enifo");
        return mav;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(code = "list",name = "企业列表")
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<EnInfo> enInfos = enInfoService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(enInfos,pageAndSort);
    }


    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增企业")
    public ResultDataDto add(@ModelAttribute("enInfo")EnInfo enInfo){
        if(enInfoService.addEntity(enInfo)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }


    /**
     * 用户完善企业信息的方法.
     */
    @RequestMapping(value = "/addByCurrentUser",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "addByCurrentUser",name = "新增企业")
    public ResultDataDto addByCurrentUser(@ModelAttribute("enInfo")EnInfo enInfo){
        enInfo.setUsrno(SecurityUtil.getCurrentUserName());
        if(enInfoService.addEntity(enInfo)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新企业")
    public ResultDataDto update(@ModelAttribute("enInfo")EnInfo enInfo){
        enInfoService.updateEntity(enInfo);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 生效
     */
    @RequestMapping(value = "/effective",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "effective",name = "企业生效")
    public ResultDataDto effective(@RequestParam("id") Integer id){
        enInfoService.updateEntityEffective(id);
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 失效
     */
    @RequestMapping(value = "/invalid",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "invalid",name = "企业生效")
    public ResultDataDto invalid(@RequestParam("id") Integer id){
        enInfoService.updateEntityInvalid(id);
        return ResultDataDto.addOperationSuccess();
    }
}
