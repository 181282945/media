package com.aisino.base.invoice.eninfo.controller;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-27.
 */
@RestController
@RequestMapping(path = UserEnInfoController.PATH)
@AclResc(id = 6000,code = "userEnInfo", name = UserEnInfoController.MODULE_NAME,homePage = UserEnInfoController.PATH,target = AclResource.Target.USERINFO)
public class UserEnInfoController extends BaseController {
    final static String PATH = "/base/invoice/eninfo/u";
    final static String MODULE_NAME = "企业信息";

    @Resource
    private EnInfoService enInfoService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;


    @GetMapping(path = "/addEnInfoMav",produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView addEnfo(){
        ModelAndView mav = new ModelAndView(PATH + "/add_enifo");
        EnInfo currentEnInfo = cuzSessionAttributes.getEnInfo();
        mav.addObject("currentEnInfo",currentEnInfo);
        return mav;
    }

    /**
     * 用户完善企业信息的方法.
     */
    @PostMapping(path = "/addOrUpdateByCurrentUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 6001,code = "addOrUpdateByCurrentUser",name = "新增或修改企业")
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public ResultDataDto addOrUpdateByCurrentUser(@ModelAttribute("enInfo")EnInfo enInfo){
        UserInfo userInfo = cuzSessionAttributes.getUserInfo();
        if(!UserInfo.SubType.ADMIN.getCode().equals(userInfo.getSubType()))
            throw new RuntimeException("只有企业管理员才能修改!");
        userInfo.setTaxNo(enInfo.getTaxno());
        if(enInfo.getId()==null){
            enInfo.setUsrno(userInfo.getUsrno());
            Integer enInfoId = enInfoService.addEntity(enInfo);
            if(enInfoId!=null){
                userInfo.setPassword(null);
                userInfoService.updateEntity(userInfo);//更新用户税号
                cuzSessionAttributes.setEnInfo(enInfoService.findEntityById(enInfoId));
            }
        }else{
            enInfo.setDelflags(BaseInvoiceEntity.DelflagsType.DELETED.getCode());
            enInfoService.updateEntity(enInfo);
            cuzSessionAttributes.getEnInfo().setDelflags(true);
            cuzSessionAttributes.setEnInfo(enInfoService.findEntityById(enInfo.getId()));
            return ResultDataDto.addUpdateSuccess();
        }
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 用户完善企业信息的方法.
     */
    @GetMapping(path = "/checkTaxNoExist",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional(readOnly = true)
    public boolean checkTaxNoExist(@RequestParam("taxNo")String taxNo){
        EnInfo enInfo = enInfoService.getByTaxNo(taxNo);
        if(enInfo == null){
            return false;
        }
        return true;
    }
}
