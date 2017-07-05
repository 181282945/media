package com.aisino.common.controller;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.params.SystemParameter;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.e9.controller.invoice.invoiceinfo.UserInvoiceInfoController;
import com.aisino.e9.controller.invoice.order.orderinfo.UserOrderInfoController;
import com.aisino.e9.service.sysmgr.parameter.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 为 on 2017-5-3.
 */
@RestController
@RequestMapping(path = CenterController.PATH)
@AclResc(id = 1100, code = "userCenter", name = CenterController.MODULE_NAME, homePage = CenterController.HOME_PAGE, target = AclResource.Target.USERINFO)
public class CenterController extends BaseController {

    final static String MODULE_NAME = "个人中心";

    final static String PATH = "/center";

    public final static String HOME_PAGE = PATH + "/userinfo";

    public final static String ENINFO_PAGE = PATH + "/eninfo";

    public final static String SETTING_PAGE = PATH + "/setting";

    public final static String EDITPASSWORD_PAGE = PATH + "/editpassword";

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Autowired
    private SystemParameter systemParameter;


    @GetMapping(value = "/userinfo", produces = MediaType.TEXT_HTML_VALUE)
    @AclResc(id = 1101, code = "userinfo", name = "个人信息")
    public ModelAndView userinfo() {
        ModelAndView mav = new ModelAndView(HOME_PAGE);
        this.fill(mav);
//        mav.addObject("ExCoSelectOption", HtmlUtil.createOption(ParamUtil.FirstOption.EDIT,parameterService.findByTypeId(1)));
        return mav;
    }

    @GetMapping(value = "/setting", produces = MediaType.TEXT_HTML_VALUE)
    @AclResc(id = 1102, code = "setting", name = "基本设置")
    public ModelAndView setting() {
        if (cuzSessionAttributes.getEnInfo() == null)
            return new ModelAndView(CenterController.ENINFO_PAGE);
        ModelAndView mav = new ModelAndView(SETTING_PAGE);
        this.fill(mav);
        mav.addObject("taxRateOptions", systemParameter.createTaxRateOption(ParamUtil.FirstOption.SELECT));
        mav.addObject("currentEnInfo",cuzSessionAttributes.getEnInfo());
//        mav.addObject("eninfoCheck",cuzSessionAttributes.eninfoCheck());
//        mav.addObject("ExCoSelectOption", HtmlUtil.createOption(ParamUtil.FirstOption.EDIT,parameterService.findByTypeId(1)));
        return mav;
    }

    @GetMapping(value = "/eninfo", produces = MediaType.TEXT_HTML_VALUE)
    @AclResc(id = 1103, code = "eninfo", name = "企业信息")
    public ModelAndView eninfo() {
        ModelAndView mav = new ModelAndView(ENINFO_PAGE);
        this.fill(mav);
        mav.addObject("currentEnInfo",cuzSessionAttributes.getEnInfo());
        mav.addObject("eninfoCheck",cuzSessionAttributes.eninfoCheck());
//        mav.addObject("ExCoSelectOption", HtmlUtil.createOption(ParamUtil.FirstOption.EDIT,parameterService.findByTypeId(1)));
        return mav;
    }

    @GetMapping(value = "/editpassword", produces = MediaType.TEXT_HTML_VALUE)
    @AclResc(id = 1104, code = "editpassword", name = "修改密码")
    public ModelAndView editpassword() {
        ModelAndView mav = new ModelAndView(EDITPASSWORD_PAGE);
        this.fill(mav);
        return mav;
    }


    /**
     * 填充通用的地址
     */
    private void fill(ModelAndView mav){
        mav.addObject("USERORDERINFO_HOME_PAGE", UserOrderInfoController.HOME_PAGE);
        mav.addObject("USERINVOICEINFO_HOME_PAGE", UserInvoiceInfoController.HOME_PAGE);
        mav.addObject("USERCENTER_HOME_PAGE", CenterController.HOME_PAGE);
        mav.addObject("ENINFO_PAGE", ENINFO_PAGE);
        mav.addObject("SETTING_PAGE", SETTING_PAGE);
        mav.addObject("EDITPASSWORD_PAGE", EDITPASSWORD_PAGE);
        mav.addObject("currentUserInfo", cuzSessionAttributes.getUserInfo());
    }

}
