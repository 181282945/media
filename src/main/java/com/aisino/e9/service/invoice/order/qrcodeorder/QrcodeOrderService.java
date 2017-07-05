package com.aisino.e9.service.invoice.order.qrcodeorder;

import com.aisino.core.service.BaseShardingService;
import com.aisino.e9.dao.invoice.order.qrcodeorder.QrcodeOrderMapper;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.qrcodeorder.pojo.QrcodeOrder;

/**
 * Created by 为 on 2017-7-4.
 */

public interface QrcodeOrderService extends BaseShardingService<QrcodeOrder,QrcodeOrderMapper> {


    /**
     *  创建二维码地址
     */
    String createQrcodeOrderUrl(Integer qrcodeOrderId,String password);


    /**
     * 扫码后,跳转到开票页面所需要的数据
     */
    OrderDetail getQrcodeMsg(Integer mark, String key,Integer shardingMark);
}
