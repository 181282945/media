package com.aisino.e9.service.invoice.order.orderinfo.impl;

import com.aisino.common.util.IdWorker;
import com.aisino.core.mybatis.specification.ShardingSpecification;
import com.aisino.core.service.impl.BaseShardingServiceImpl;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderinfo.vo.OrderInfoVo;
import com.aisino.e9.entity.invoice.order.qrcodeorder.pojo.QrcodeOrder;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import com.aisino.e9.dao.invoice.order.orderinfo.OrderInfoMapper;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.core.entity.BaseInvoiceEntity.DelflagsType;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.e9.service.invoice.order.qrcodeorder.QrcodeOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderInfoService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrderInfoServiceImpl extends BaseShardingServiceImpl<OrderInfo, OrderInfoMapper> implements OrderInfoService {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private QrcodeOrderService qrcodeOrderService;

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void validateAddEntity(OrderInfo entity) {
        super.validateAddEntity(entity);
        if (StringUtils.trimToNull(entity.getUsrno()) == null)
            entity.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());
        if (StringUtils.trimToNull(entity.getStatus()) == null)
            entity.setStatus(OrderInfo.StatusType.NOTYET.getCode());

    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        ShardingSpecification<OrderInfo> specification = new ShardingSpecification<>(OrderInfo.class, cuzSessionAttributes.getEnInfo().getShardingId());
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
        return this.getOne(specification);
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo,Integer shardingId) {
        ShardingSpecification<OrderInfo> specification = new ShardingSpecification<>(OrderInfo.class, shardingId);
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
        return this.getOne(specification);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void fillUsrname(List<OrderInfoVo> orderInfoVosr) {
        for (OrderInfoVo orderInfoVo : orderInfoVosr) {
            UserInfo userInfo = userInfoService.getUserByUsrno(orderInfoVo.getUsrno());
            if (userInfo != null)
                orderInfoVo.setUsrName(userInfo.getUsrname());
            orderInfoVo.setTaxName(cuzSessionAttributes.getEnInfo().getTaxname());
        }
    }

    @Override
    public String addOrderByQrcode(String qrcodeItemName, String qrcodeItemPrice) {
        if (StringUtils.trimToNull(qrcodeItemName) == null)
            throw new RuntimeException("请填写开票项目!");
        if (StringUtils.trimToNull(qrcodeItemPrice) == null)
            throw new RuntimeException("请填写消费金额!");
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(String.valueOf(idWorker.nextId()));
        orderInfo.setTaxno(cuzSessionAttributes.getEnInfo().getTaxno());
        orderInfo.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());
        orderInfo.setType(OrderInfo.Type.QRCODE.getCode());
        orderInfo.setMajorItems(qrcodeItemName);
        orderInfo.setDkflags(OrderInfo.DkflagsType.SELF.getCode());
        orderInfo.setBuyerType(OrderInfo.BuyerType.INDIVIDUAL.getCode());
        orderInfoService.addEntity(orderInfo);


        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setInvoiceNature(OrderDetail.InvoiceNature.NORMAL.getCode());
        orderDetail.setAmount(qrcodeItemPrice);
        orderDetail.setItemName(qrcodeItemName);
        orderDetail.setTaxIncluded(OrderDetail.TaxIncludedFlag.INCLUDE.getCode());
        orderDetail.setTaxRate(cuzSessionAttributes.getEnInfo().getQrcodeTaxRate());
        orderDetail.setOrderNo(orderInfo.getOrderNo());
        orderDetail.setTax(new BigDecimal(qrcodeItemPrice).multiply(new BigDecimal(orderDetail.getTaxRate())).toString());
        orderDetail.setItemTaxCode(cuzSessionAttributes.getEnInfo().getQrcodeTaxClassCode());
        orderDetailService.addEntity(orderDetail);

        QrcodeOrder qrcodeOrder = new QrcodeOrder();
        qrcodeOrder.setOrderNo(orderInfo.getOrderNo());
        qrcodeOrder.setStatus(QrcodeOrder.Status.NOTYET.getCode());
        String password = String.valueOf((int) ((Math.random() * 9 + 1) * 10000000));//随机生成8位密码
        qrcodeOrder.setPassword(password);
        Integer qrcodeOrderId = qrcodeOrderService.addEntity(qrcodeOrder);
        qrcodeOrder.setId(qrcodeOrderId);
        String url = qrcodeOrderService.createQrcodeOrderUrl(qrcodeOrderId, bCryptPasswordEncoder.encode(password));
        qrcodeOrder.setQrcodeUrl(url);
        qrcodeOrderService.updateEntity(qrcodeOrder, qrcodeOrder.getShardingId());
        return url;

    }

    public Map<Integer, String> invalidBatch(final Integer[] orderIds) {
        if (orderIds.length == 0)
            throw new RuntimeException("请选择删除订单!");
        Map<Integer, String> map = new HashMap<>();
        for (Integer orderId : orderIds) {
            OrderInfo orderInfo = orderInfoService.findEntityById(orderId, cuzSessionAttributes.getEnInfo().getShardingId());

            if (OrderInfo.StatusType.ALREADY.getCode().equals(orderInfo.getStatus())) {
                map.put(orderId, "单号:" + orderInfo.getOrderNo() + "已开票,无法删除改订单!");
                continue;
            }

            if (orderDetailService.existAlreadyDetailByOrderNo(orderInfo.getOrderNo())) {
                map.put(orderId, "单号:" + orderInfo.getOrderNo() + "已部分开票,无法删除改订单!");
                continue;
            }

            String delOrderNo = orderInfo.getOrderNo() + "_" + System.currentTimeMillis();
            // 作废订单
            orderInfo.setOrderNo(orderInfo.getOrderNo() + "_" + System.currentTimeMillis());
            orderInfo.setDelflags(DelflagsType.DELETED.getCode());
            orderInfoService.updateEntity(orderInfo, cuzSessionAttributes.getEnInfo().getShardingId());
            // 作废明细
            List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(delOrderNo);
            for (OrderDetail orderDetail : orderDetails) {
                orderDetailService.updateEntityInvalid(orderDetail.getId(), cuzSessionAttributes.getEnInfo().getShardingId());
            }
        }
        return map;
    }

    @Override
    public List<OrderInfoVo> findPageOrderInfo(OrderInfoVo orderInfoVo, PageAndSort pageAndSort) {
        if (UserInfo.SubType.ADMIN.getCode().equals(cuzSessionAttributes.getUserInfo().getSubType())) {//判断是否管理员
            orderInfoVo.setTaxno(cuzSessionAttributes.getEnInfo().getTaxno());
        } else {
            orderInfoVo.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());//如果不是的话,只能查询自己的
        }
        orderInfoVo.setShardingId(cuzSessionAttributes.getEnInfo().getShardingId());
        pageAndSort.setRowCount(getMapper().findPageOrderInfoCount(orderInfoVo));
        return getMapper().findPageOrderInfo(orderInfoVo, pageAndSort);
    }
}
