package com.aisino.base.sysmgr.aclrescuser.controller;

import com.aisino.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.aisino.base.sysmgr.aclrescuser.entity.AclRescUser;
import com.aisino.base.sysmgr.aclrescuser.service.AclRescUserService;
import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
@RestController
@RequestMapping(value = AclRescUserController.PATH)
@AclResc(code = "aclRescUser", name = AclRescUserController.MODULE_NAME,homePage = AclRescUserController.HOME_PAGE,target = AclResourceTarget.ACLUSER)
public class AclRescUserController extends BaseController<AclRescUser> {
    final static String PATH = "/base/sysmgr/aclrescuser";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "用户资源管理";

    @Resource
    private AclRescUserService aclRescUserService;



    //页面模板路径
    private static final String VIEW_NAME = "/list_aclrescuser";
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
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        return mav;
    }



    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(code = "list",name = "用户资源列表")
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclRescUser> aclRescUsers = aclRescUserService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclRescUsers,pageAndSort);
    }


    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增用户资源")
    public ResultDataDto add(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        if(aclRescUserService.addEntity(aclRescUser)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新用户资源")
    public ResultDataDto update(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        aclRescUserService.updateEntity(aclRescUser);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "delete",name = "删除用户资源")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclRescUserService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }

    /**
     * 根据资源角色新增
     */
    @RequestMapping(value = "/addByRescIdUserId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "addByRescIdUserId",name = "根据资源角色新增")
    public ResultDataDto addByRescIdUserId(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        if(aclRescUserService.addEntity(aclRescUser)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 根据资源角色删除
     */
    @RequestMapping(value = "/deleteByRescIdUserId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "deleteByRescIdUserId",name = "根据资源用户删除")
    public ResultDataDto deleteByRescIdUserId(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        aclRescUserService.deleteByRescIdUserId(aclRescUser.getUserId(),aclRescUser.getRescId());
        return ResultDataDto.addDeleteSuccess();
    }

}
