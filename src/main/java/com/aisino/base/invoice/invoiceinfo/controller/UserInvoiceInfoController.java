package com.aisino.base.invoice.invoiceinfo.controller;

import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.base.invoice.invoiceinfo.service.InvoiceInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.annotation.CuzDataSource;
import com.aisino.common.controller.IndexController;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-5-5.
 */
@RestController
@RequestMapping(value = UserInvoiceInfoController.PATH)
@AclResc(id = 30000,code = "userOrderInfo", name = UserInvoiceInfoController.MODULE_NAME,homePage = UserInvoiceInfoController.HOME_PAGE,target = AclResource.Target.USERINFO)
public class UserInvoiceInfoController extends BaseController<InvoiceInfo> {

    final static String PATH = "/base/invoice/invoiceinfo/u";

    public final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "发票管理";


    //页面模板路径
    private static final String VIEW_NAME = "/list_invoiceinfo";
    //修改更新
//    private static final String UPDATE_URL = PATH + "/update";
//    //新增
//    private static final String ADD_URL = PATH + "/add";
//    //删除
//    private static final String DELETE_URL = PATH + "/invalid";
    //查询
    private static final String SEARCH_URL = PATH + "/list";

    @Resource
    private InvoiceInfoService invoiceInfoService;


    @RequestMapping(value = "/tolist",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("userName", SecurityUtil.getCurrentUserName());
        mav.addObject("INDEX_HOME_PAGE", IndexController.HOME_PAGE);
        mav.addObject("USERORDERINFO_HOME_PAGE", UserInvoiceInfoController.HOME_PAGE);
        mav.addObject("MODULE_NAME",MODULE_NAME);
//        mav.addObject("UPDATE_URL",UPDATE_URL);
//        mav.addObject("ADD_URL",ADD_URL);
//        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        return mav;
    }


    /**
     * 用户发票查询列表
     */
    @CuzDataSource
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 30001,code = "list",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<InvoiceInfo> invoiceInfos = invoiceInfoService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(invoiceInfos,pageAndSort);
    }

}
