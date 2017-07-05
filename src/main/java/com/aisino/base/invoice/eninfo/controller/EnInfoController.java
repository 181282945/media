package com.aisino.base.invoice.eninfo.controller;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
@RestController
@RequestMapping(path = EnInfoController.PATH)
@AclResc(id = 4000, code = "eninfo", name = EnInfoController.MODULE_NAME, homePage = EnInfoController.HOME_PAGE, target = AclResource.Target.ACLUSER)
public class EnInfoController extends BaseController {
    final static String PATH = "/base/invoice/eninfo/a";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "企业信息管理";

    @Resource
    private EnInfoService enInfoService;

    @Resource
    private UserInfoService userInfoService;


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


    @GetMapping(path = "/tolist", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList() {
        ModelAndView mav = generalMav(PATH, MODULE_NAME, VIEW_NAME, UPDATE_URL, ADD_URL, DELETE_URL, SEARCH_URL);
        mav.addObject("billingOptionTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.QUERY,EnInfo.BillingOptionType.getParams()));
        mav.addObject("yhzcbsTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.QUERY,EnInfo.YhzcbsType.getParams()));
        mav.addObject("lslbsTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.QUERY,EnInfo.LslbsType.getParams()));
        mav.addObject("delflagsTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.QUERY,BaseInvoiceEntity.DelflagsType.getParams()));
        return mav;
    }


    @GetMapping(path = "/list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 4001, code = "list", name = "企业列表")
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort") PageAndSort pageAndSort) {
        List<EnInfo> enInfos = enInfoService.findByJqgridFilters(jqgridFilters, pageAndSort);
        return new ResultDataDto(enInfos, pageAndSort);
    }


    /**
     * 新增
     */
    @PostMapping(path = "/add",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 4002, code = "add", name = "新增企业")
    public ResultDataDto add(@ModelAttribute("enInfo") EnInfo enInfo) {
        if (enInfoService.addEntity(enInfo) != null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }


    /**
     * 更新
     */
    @PostMapping(path = "/update",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 4003, code = "update", name = "更新企业")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto update(@ModelAttribute("enInfo") EnInfo enInfo) {
        enInfoService.updateEntity(enInfo);
        if (null != enInfo.getTaxno()) {
            UserInfo userInfo = userInfoService.getUserByUsrno(enInfo.getUsrno());
            userInfo.setTaxNo(enInfo.getTaxno());
            userInfoService.updateEntity(userInfo);
        }
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 生效
     */
    @PostMapping(path = "/effective", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 4004, code = "effective", name = "企业生效")
    public ResultDataDto effective(@RequestParam("id") Integer id) {
        enInfoService.updateEntityEffective(id);
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 失效
     */
    @PostMapping(path = "/invalid", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 4005, code = "invalid", name = "企业生效")
    public ResultDataDto invalid(@RequestParam("id") Integer id) {
        enInfoService.updateEntityInvalid(id);
        return ResultDataDto.addOperationSuccess();
    }
}
