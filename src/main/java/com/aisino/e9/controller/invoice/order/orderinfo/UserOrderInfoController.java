package com.aisino.e9.controller.invoice.order.orderinfo;

import com.aisino.common.controller.CenterController;
import com.aisino.common.params.SystemParameter;
import com.aisino.core.util.LocalError;
import com.aisino.e9.controller.invoice.invoiceinfo.UserInvoiceInfoController;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.entity.invoice.order.orderinfo.vo.OrderInfoVo;
import com.aisino.e9.entity.invoice.order.qrcodeorder.vo.QrcodeOrderVo;
import com.aisino.e9.service.invoice.invoiceinfo.QrcodeInvoiceService;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.controller.IndexController;
import com.aisino.common.util.CalendarUtil;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-27. 前台用户用的订单信息控制器
 */
@RestController
@RequestMapping(path = UserOrderInfoController.PATH)
@AclResc(id = 2000, code = "userOrderInfo", name = UserOrderInfoController.MODULE_NAME, homePage = UserOrderInfoController.HOME_PAGE, target = AclResource.Target.USERINFO)
public class UserOrderInfoController extends BaseController {

    final static String PATH = "/base/invoice/order/orderinfo/u";

    public final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "订单仓库";

    // 页面模板路径
    private static final String VIEW_NAME = "/list_userorderinfo";
    // 修改更新
    private static final String UPDATE_URL = PATH + "/update";
    // 新增
    private static final String ADD_URL = PATH + "/add";
    // 删除
    private static final String DELETE_URL = PATH + "/invalid";
    // 查询
    private static final String SEARCH_URL = PATH + "/list";

    @Resource
    private OrderInfoService orderInfoService;

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @GetMapping(path = "/tolist", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList() {
        if (!cuzSessionAttributes.eninfoCheck())
            return new ModelAndView("redirect:" + CenterController.ENINFO_PAGE);
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("currentUserInfo", cuzSessionAttributes.getUserInfo());
        mav.addObject("INDEX_HOME_PAGE", IndexController.HOME_PAGE);
        mav.addObject("enInfo", cuzSessionAttributes.getEnInfo());
        mav.addObject("USERORDERINFO_HOME_PAGE", UserOrderInfoController.HOME_PAGE);
        mav.addObject("USERINVOICEINFO_HOME_PAGE", UserInvoiceInfoController.HOME_PAGE);
        mav.addObject("USERCENTER_HOME_PAGE", CenterController.HOME_PAGE);
        mav.addObject("DEFAULT_QRCODEITEMNAME", cuzSessionAttributes.getEnInfo().getQrcodeItemName());
        mav.addObject("MODULE_NAME", MODULE_NAME);
        mav.addObject("UPDATE_URL", UPDATE_URL);
        mav.addObject("ADD_URL", ADD_URL);
        mav.addObject("DELETE_URL", DELETE_URL);
        mav.addObject("SEARCH_URL", SEARCH_URL);
        mav.addObject("orderInfoStatusParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.EDIT, OrderInfo.StatusType.getParams()));
        mav.addObject("orderInfoTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.EDIT, OrderInfo.Type.getParams()));
        mav.addObject("orderInfoBuyerTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.EDIT, OrderInfo.BuyerType.getParams()));
        mav.addObject("invoiceNatureParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.EDIT, OrderDetail.InvoiceNature.getParams()));
        mav.addObject("dkflagsSelectString", OrderInfo.DkflagsType.getSelect(ParamUtil.FirstOption.EDIT));
        mav.addObject("buyerTypeSelectString", OrderInfo.BuyerType.getSelect(ParamUtil.FirstOption.EDIT));
        mav.addObject("selectString", OrderInfo.StatusType.getSelect(ParamUtil.FirstOption.QUERY));
        mav.addObject("invoiceNatureOptions", OrderDetail.InvoiceNature.getSelect(ParamUtil.FirstOption.EDIT));
        return mav;
    }

    /**
     * 用户订单查询列表
     */
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2001, code = "list", name = "查询列表")
    @Transactional(readOnly = true)
    public ResultDataDto list(@ModelAttribute OrderInfoVo orderInfoVo, @ModelAttribute("pageAndSort") PageAndSort pageAndSort) {
        orderInfoVo.setDkflags(0);
        if (orderInfoVo != null && orderInfoVo.getEndDate() != null) {
            //根据系统需求而定,云开票需要包含查询条件最后一天
            orderInfoVo.setEndDate(CalendarUtil.getDayAgo(CalendarUtil.parseGregorianCalendar(orderInfoVo.getEndDate()), -1).getTime());
        }
        List<OrderInfoVo> orderInfoVos = orderInfoService.findPageOrderInfo(orderInfoVo, pageAndSort);
        orderInfoService.fillUsrname(orderInfoVos);
        return new ResultDataDto(orderInfoVos, pageAndSort);
    }

    /**
     * 新增
     */
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2002, code = "add", name = "新增订单")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto add(@ModelAttribute("orderInfo") OrderInfo orderInfo) {
        if (orderInfoService.addEntity(orderInfo) != null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     */
    @PostMapping(path = "/view", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2003, code = "view", name = "查看")
    @Transactional(readOnly = true)
    public ResultDataDto view(@RequestParam("id") Integer id) {
        return new ResultDataDto(orderInfoService.findEntityById(id, cuzSessionAttributes.getEnInfo().getShardingId()));
    }

    /**
     * 更新
     */
    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2004, code = "update", name = "更新订单")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto update(@ModelAttribute("orderInfo") OrderInfo orderInfo) {
        orderInfoService.updateEntity(orderInfo, cuzSessionAttributes.getEnInfo().getShardingId());
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 生效
     */
    @PostMapping(path = "/effective", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2006, code = "effective", name = "订单生效")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto effective(@RequestParam("id") Integer id) {
        orderInfoService.updateEntityEffective(id, cuzSessionAttributes.getEnInfo().getShardingId());
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 批量失效
     */
    @PostMapping(path = "/invalidBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2007, code = "invalidBatch", name = "订单失效")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto invalidBatch(@RequestParam("orderIds") Integer[] orderIds) {
        Map<Integer, String> map = orderInfoService.invalidBatch(orderIds);
        ResultDataDto resultDataDto = ResultDataDto.addOperationSuccess();
        StringBuilder stringBuilder = new StringBuilder();
        if (map.size() > 0) {
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                stringBuilder.append(entry.getValue() + "</br>");
            }
            resultDataDto.setMessage("错误信息:</br>" + stringBuilder.toString());
        }
        return resultDataDto;
    }


    /**
     * 二维码开票,订单保存,然后返回开票接口地址
     */
    @PostMapping(path = "/addOrderByQrcode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 2008, code = "addOrderByQrcode", name = "保存二维码开票订单")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
    public ResultDataDto addOrderByQrcode(@RequestParam("qrcodeItemName") String qrcodeItemName, @RequestParam("qrcodeItemPrice") String qrcodeItemPrice) {
        String qrcodeOrderUrl = orderInfoService.addOrderByQrcode(qrcodeItemName, qrcodeItemPrice);
        return ResultDataDto.addOperationSuccess().setDatas(qrcodeOrderUrl);
    }

}
