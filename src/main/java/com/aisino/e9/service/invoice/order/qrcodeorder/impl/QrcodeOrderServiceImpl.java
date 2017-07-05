package com.aisino.e9.service.invoice.order.qrcodeorder.impl;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.params.SystemParameter;
import com.aisino.core.service.impl.BaseShardingServiceImpl;
import com.aisino.e9.dao.invoice.order.qrcodeorder.QrcodeOrderMapper;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.entity.invoice.order.qrcodeorder.pojo.QrcodeOrder;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.e9.service.invoice.order.qrcodeorder.QrcodeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-7-4.
 */
@Service("qrcodeOrderService")
public class QrcodeOrderServiceImpl extends BaseShardingServiceImpl<QrcodeOrder, QrcodeOrderMapper> implements QrcodeOrderService {

    @Autowired
    private SystemParameter systemParameter;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private OrderInfoService orderInfoService;

    @Override
    public String createQrcodeOrderUrl(Integer qrcodeOrderId, String password) {
        return systemParameter.getQrcodeUrl() + "?mark=".intern() + qrcodeOrderId + "&key=".intern() + password + "&shardingMark="+cuzSessionAttributes.getEnInfo().getShardingId();
    }

    @Override
    public OrderDetail getQrcodeMsg(Integer mark,String key,Integer shardingMark){
        QrcodeOrder  qrcodeOrder= this.findEntityById(mark,shardingMark);
        EnInfo enInfo = new EnInfo();
        enInfo.setShardingId(shardingMark);
        cuzSessionAttributes.setEnInfo(enInfo);
         if(!bCryptPasswordEncoder.matches(qrcodeOrder.getPassword(),key))
             throw new RuntimeException("秘钥验证失败!");
        OrderInfo orderInfo = orderInfoService.getByOrderNo(qrcodeOrder.getOrderNo());
        if(!OrderInfo.Type.QRCODE.getCode().equals(orderInfo.getType()))
            throw new RuntimeException("此单不支持二维码开票!");
        List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(qrcodeOrder.getOrderNo());

        return orderDetails.get(0);
    }

}
