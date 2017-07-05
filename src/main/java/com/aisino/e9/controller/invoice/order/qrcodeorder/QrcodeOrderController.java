package com.aisino.e9.controller.invoice.order.qrcodeorder;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.util.LocalError;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.entity.invoice.order.qrcodeorder.vo.QrcodeOrderVo;
import com.aisino.e9.service.invoice.invoiceinfo.QrcodeInvoiceService;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.e9.service.invoice.order.qrcodeorder.QrcodeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-7-5.
 * 二维码开票控制器
 */
@RestController
@RequestMapping(path = QrcodeOrderController.PATH)
public class QrcodeOrderController extends BaseController {


    final static String PATH = "/base/invoice/order/qrcodeorder";

    @Resource
    private QrcodeOrderService qrcodeOrderService;

    @Resource
    private QrcodeInvoiceService qrcodeInvoiceService;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private EnInfoService enInfoService;

    @Resource
    private AuthCodeInfoService authCodeInfoService;

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;


    @GetMapping(path = "/getQrcodeMsg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ModelAndView getQrcodeMsg(@RequestParam("mark") Integer mark, @RequestParam("key") String key,@RequestParam("shardingMark")Integer shardingMark) {
        OrderDetail orderDetail = qrcodeOrderService.getQrcodeMsg(mark, key,shardingMark);
        ModelAndView mav = new ModelAndView("/mobile/qrcodeorder/billing");
        mav.addObject("qrcodeItemName", orderDetail.getItemName());
        mav.addObject("qrcodeItemPrice", orderDetail.getAmount());
        mav.addObject("mark", mark);
        mav.addObject("shardingMark", shardingMark);
        mav.addObject("BACK_URL",qrcodeOrderService.createQrcodeOrderUrl(mark,key));
        return mav;
    }


    /**
     * 开票验证,成功跳转到确认开票页面
     */
    @PostMapping(path = "/verification", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional(readOnly = true)
    public ModelAndView verification(@ModelAttribute("qrcodeOrderVo") QrcodeOrderVo qrcodeOrderVo) {
        qrcodeInvoiceService.verification(qrcodeOrderVo);
        ModelAndView mav = new ModelAndView("/mobile/qrcodeorder/billing_confirm");
        mav.addObject("qrcodeOrderVo");
        return mav;
    }


    /**
     * 扫码开票接口
     */
    @PostMapping(path = "/billingByQrcode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto billingByQrcode(@ModelAttribute("qrcodeOrderVo") QrcodeOrderVo qrcodeOrderVo) {
        LocalError.getMapMessage().get().clear();
        OrderInfo orderInfo = orderInfoService.getByOrderNo(qrcodeOrderVo.getOrderNo(),qrcodeOrderVo.getShardingId());
        EnInfo enInfo = enInfoService.getByTaxNo(orderInfo.getTaxno());
        cuzSessionAttributes.setEnInfo(enInfo);
        AuthCodeInfo authCodeInfo = authCodeInfoService.getByTaxNo(orderInfo.getTaxno());
        cuzSessionAttributes.setAuthCodeInfo(authCodeInfo);
        if (!qrcodeInvoiceService.billingByQrcode(qrcodeOrderVo,orderInfo)) {
            String key = LocalError.getMapMessage().get().keySet().iterator().next();
            throw new RuntimeException(LocalError.getMapMessage().get().get(key));
        }
        orderInfo.setStatus(OrderInfo.StatusType.ALREADY.getCode());
        orderInfoService.updateEntity(orderInfo,qrcodeOrderVo.getShardingId());
        return ResultDataDto.addOperationSuccess();
    }
}
