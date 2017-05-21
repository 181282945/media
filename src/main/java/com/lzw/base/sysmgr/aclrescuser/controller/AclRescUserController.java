package com.lzw.base.sysmgr.aclrescuser.controller;

import com.lzw.base.sysmgr.aclrescuser.entity.AclRescUser;
import com.lzw.base.sysmgr.aclrescuser.service.AclRescUserService;
import com.lzw.base.sysmgr.aclresource.entity.AclResource;
import com.lzw.core.dto.ResultDataDto;
import com.lzw.core.mybatis.specification.PageAndSort;
import com.lzw.base.sysmgr.aclresource.annotation.AclResc;
import com.lzw.common.dto.jqgrid.JqgridFilters;
import com.lzw.core.controller.BaseController;
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
@AclResc(id = 5000,code = "aclRescUser", name = AclRescUserController.MODULE_NAME,homePage = AclRescUserController.HOME_PAGE)
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
        ModelAndView mav = generalMav(PATH,MODULE_NAME,VIEW_NAME,UPDATE_URL,ADD_URL,DELETE_URL,SEARCH_URL);
        return mav;
    }



    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @AclResc(id = 5001,code = "list",name = "用户资源列表")
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclRescUser> aclRescUsers = aclRescUserService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclRescUsers,pageAndSort);
    }


    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5002,code = "add",name = "新增用户资源")
    public ResultDataDto add(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        if(aclRescUserService.addEntity(aclRescUser)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5003,code = "update",name = "更新用户资源")
    public ResultDataDto update(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        aclRescUserService.updateEntity(aclRescUser);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5004,code = "delete",name = "删除用户资源")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclRescUserService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }

    /**
     * 根据资源角色新增
     */
    @RequestMapping(value = "/addByRescIdUserId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5005,code = "addByRescIdUserId",name = "根据资源角色新增")
    public ResultDataDto addByRescIdUserId(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        if(aclRescUserService.addEntity(aclRescUser)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 根据资源角色删除
     */
    @RequestMapping(value = "/deleteByRescIdUserId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5006,code = "deleteByRescIdUserId",name = "根据资源用户删除")
    public ResultDataDto deleteByRescIdUserId(@ModelAttribute("aclRescUser")AclRescUser aclRescUser){
        aclRescUserService.deleteByRescIdUserId(aclRescUser.getUserId(),aclRescUser.getRescId());
        return ResultDataDto.addDeleteSuccess();
    }

}
