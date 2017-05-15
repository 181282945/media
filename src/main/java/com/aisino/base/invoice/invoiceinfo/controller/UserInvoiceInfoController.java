package com.aisino.base.invoice.invoiceinfo.controller;

import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.base.invoice.invoiceinfo.service.InvoiceInfoService;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.annotation.CuzDataSource;
import com.aisino.common.controller.IndexController;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.common.model.xml.impl.KpRequestyl;
import com.aisino.common.util.IOUtil;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.DataSourceContextHolder;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 为 on 2017-5-5.
 */
@RestController
@RequestMapping(value = UserInvoiceInfoController.PATH)
@AclResc(id = 30000, code = "userOrderInfo", name = UserInvoiceInfoController.MODULE_NAME, homePage = UserInvoiceInfoController.HOME_PAGE, target = AclResource.Target.USERINFO)
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

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private PlatformTransactionManager platformTransactionManager;


    @RequestMapping(value = "/tolist", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList() {
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("userName", SecurityUtil.getCurrentUserName());
        mav.addObject("INDEX_HOME_PAGE", IndexController.HOME_PAGE);
        mav.addObject("USERORDERINFO_HOME_PAGE", UserInvoiceInfoController.HOME_PAGE);
        mav.addObject("MODULE_NAME", MODULE_NAME);
//        mav.addObject("UPDATE_URL",UPDATE_URL);
//        mav.addObject("ADD_URL",ADD_URL);
//        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL", SEARCH_URL);
        mav.addObject("invoiceTypeParams", ParamUtil.JqgridSelectVal(InvoiceInfo.InvoiceType.getParams()));
        return mav;
    }


    /**
     * 用户发票查询列表
     */
    @CuzDataSource
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 30001, code = "list", name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort") PageAndSort pageAndSort) {
        List<InvoiceInfo> invoiceInfos = invoiceInfoService.findByJqgridFilters(jqgridFilters, pageAndSort);
        return new ResultDataDto(invoiceInfos, pageAndSort);
    }

    /**
     * 开票
     */
    @RequestMapping(value = "/billing", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 30002, code = "billing", name = "开票")
    public ResultDataDto billing(@RequestParam("orderId") final Integer orderId) {
        if (invoiceInfoService.requestBilling(SecurityUtil.getCurrentUserNo(), orderId, InvoiceInfo.InvoiceType.NORMAL, KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL,null) != null) {
            DataSourceContextHolder.user();
            orderInfoService.updateEntityStatus(orderId, OrderInfo.StatusType.ALREADY.getCode().toString());
            return ResultDataDto.addOperationSuccess("开票成功");
        }
        return ResultDataDto.addOperationFailure();
    }

    /**
     * 下载发票
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 30003, code = "download", name = "下载")
    public void download(@RequestParam("id") Integer id, HttpServletResponse response) {
        String pdfUrl = invoiceInfoService.downloadRequest(id, SecurityUtil.getCurrentUserNo());
        IOUtil.downLoadByUrl(pdfUrl, response);
    }

    /**
     * 冲红
     */
    @RequestMapping(value = "/doRed", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 30004, code = "doRed", name = "冲红")
    public ResultDataDto doRed(@RequestParam("invoiceId") final Integer invoiceId,@RequestParam("reMarks") final String reMarks) {
        DataSourceContextHolder.user();
        final InvoiceInfo[] invoiceInfo = new InvoiceInfo[1];
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setReadOnly(true);
        OrderInfo orderInfo = transactionTemplate.execute(new TransactionCallback<OrderInfo>() {
            @Override
            public OrderInfo doInTransaction(TransactionStatus status) {
                invoiceInfo[0] = invoiceInfoService.findEntityById(invoiceId);
                return orderInfoService.getByOrderNo(invoiceInfo[0].getOrderNo());
            }
        });
        DataSourceContextHolder.write();

        final Integer redId = invoiceInfoService.requestBilling(SecurityUtil.getCurrentUserNo(), orderInfo.getId(), InvoiceInfo.InvoiceType.RED, KpRequestyl.FpkjxxFptxx.CzdmType.RETURN_RED,reMarks);
        ResultDataDto resultDataDto;
        DataSourceContextHolder.user();

        if (redId != null) {
            resultDataDto = ResultDataDto.addOperationSuccess("冲红成功");
            invoiceInfo[0].setRedflags(InvoiceInfo.RedflagsType.ALREADY.getCode());
            transactionTemplate = new TransactionTemplate(platformTransactionManager);
            transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    invoiceInfoService.updateEntity(invoiceInfo[0]);
                    InvoiceInfo red = invoiceInfoService.findEntityById(redId);
                    red.setReMarks(reMarks);
                    invoiceInfoService.updateEntity(red);
                }
            });
        }

        return ResultDataDto.addOperationSuccess();
    }

}
