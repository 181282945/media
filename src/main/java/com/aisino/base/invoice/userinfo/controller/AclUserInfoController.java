package com.aisino.base.invoice.userinfo.controller;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import com.aisino.base.sysmgr.acluserrole.entity.AclUserRole;
import com.aisino.base.sysmgr.acluserrole.service.AclUserRoleService;
import com.aisino.common.params.SystemParameter;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.entity.BaseBusinessEntity;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
@RestController
@RequestMapping(path = AclUserInfoController.PATH)
@AclResc(id = 900, code = "aclUserInfo", name = AclUserInfoController.MODULE_NAME, homePage = AclUserInfoController.HOME_PAGE, target = AclResource.Target.ACLUSER)
public class AclUserInfoController extends BaseController {
    final static String PATH = "/base/invoice/userinfo/a";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "用户管理";

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclUserRoleService aclUserRoleService;

    @Resource
    private SystemParameter systemParameter;

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


    @InitBinder("enInfo")
    public void initBinderEnInfo(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("enInfo.");
    }

    @InitBinder("userInfo")
    public void initBinderUserInfo(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("userInfo.");
    }

    @GetMapping(path = "/tolist",  produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList() {
        ModelAndView mav = generalMav(PATH, MODULE_NAME, VIEW_NAME, UPDATE_URL, ADD_URL, DELETE_URL, SEARCH_URL);
        mav.addObject("userInfoRoleParamsEdit", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.EDIT,aclRoleService.getUserInfoRoleParams()));
        mav.addObject("delflagsTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.QUERY,BaseBusinessEntity.DelflagsType.getParams()));
        return mav;
    }


    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 901, code = "list", name = "用户列表")
    public ResultDataDto list(@ModelAttribute("userInfo") UserInfo userInfo, @ModelAttribute("pageAndSort") PageAndSort pageAndSort) {
        List<UserInfo> userInfos = userInfoService.findPageUserInfo(userInfo,pageAndSort);
        userInfoService.fillRoleId(userInfos);
        return new ResultDataDto(userInfos, pageAndSort);
    }


    /**
     * 新增
     */
    @PostMapping(path = "/add",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 902, code = "add", name = "新增用户")
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public ResultDataDto add(@ModelAttribute("userInfo") UserInfo userInfo, @ModelAttribute("enInfo")EnInfo enInfo, @RequestParam("repeatPassword")String repeatPassword) {
        userInfoService.addByAclUser(userInfo,repeatPassword,enInfo);
        return ResultDataDto.addAddSuccess();
    }

    /**
     * 更新
     */
    @PostMapping(path = "/update",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 903, code = "update", name = "更新用户")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto update(@ModelAttribute("userInfo") UserInfo userInfo,@RequestParam("pwdreset")boolean pwdreset) {
        if(pwdreset)
            userInfo.setPassword(systemParameter.getDefaultUserInfoPwd());

        userInfoService.updateEntity(userInfo);
        if (null != userInfo.getRoleId()) {
            //目前不考虑多角色
            aclUserRoleService.deleteAllRoleByUserId(userInfo.getId());
            AclUserRole aclUserRole = new AclUserRole(userInfo.getId(), userInfo.getRoleId());
            aclUserRoleService.addEntity(aclUserRole);
        }
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 生效
     */
    @PostMapping(path = "/effective", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 904, code = "effective", name = "用户生效")
    public ResultDataDto effective(@RequestParam("id") Integer id) {
        userInfoService.updateEntityEffective(id);
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 失效
     */
    @PostMapping(path = "/invalid",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 905, code = "invalid", name = "用户失效")
    public ResultDataDto invalid(@RequestParam("id") Integer id) {
        userInfoService.updateEntityInvalid(id);
        return ResultDataDto.addOperationSuccess();
    }
}
