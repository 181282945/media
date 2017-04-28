package com.aisino.base.invoice.eninfo.controller;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;
import com.aisino.base.sysmgr.dbinfo.service.DbInfoService;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-27.
 */
@RestController
@RequestMapping(value = UserEnInfoController.PATH)
@AclResc(code = "userEnInfo", name = UserEnInfoController.MODULE_NAME,homePage = UserEnInfoController.PATH,target = AclResourceTarget.USERINFO)
public class UserEnInfoController extends BaseController<EnInfo> {
    final static String PATH = "/base/invoice/eninfo/u";
    final static String MODULE_NAME = "企业信息";

    @Resource
    private EnInfoService enInfoService;

    @Resource
    private DbInfoService dbInfoService;

    @Resource
    private UserInfoService userInfoService;


    @RequestMapping(value = "/addEnInfoMav",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView addEnfo(){
        ModelAndView mav = new ModelAndView(PATH + "/add_enifo");
        return mav;
    }



    /**
     * 用户完善企业信息的方法.
     */
    @RequestMapping(value = "/addByCurrentUser",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "addByCurrentUser",name = "新增企业")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto addByCurrentUser(@ModelAttribute("enInfo")EnInfo enInfo){
        UserInfo userInfo = SecurityUtil.getCurrentUserInfo();
        enInfo.setUsrno(userInfo.getUsrno());
        userInfo.setTaxNo(enInfo.getTaxno());
        if(enInfoService.addEntity(enInfo)!=null){
            userInfoService.updateEntity(userInfo);//更新用户税号
            if(dbInfoService.addByTaxNo(enInfo.getTaxno()) != null)
                return ResultDataDto.addAddSuccess();
            return ResultDataDto.addOperationFailure("保存失败!原因:建库失败");
        }
        return ResultDataDto.addOperationFailure("保存失败!");
    }
}
