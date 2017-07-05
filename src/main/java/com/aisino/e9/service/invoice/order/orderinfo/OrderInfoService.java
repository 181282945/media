package com.aisino.e9.service.invoice.order.orderinfo;

import com.aisino.core.service.BaseShardingService;
import com.aisino.e9.dao.invoice.order.orderinfo.OrderInfoMapper;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.e9.entity.invoice.order.orderinfo.vo.OrderInfoVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-25.
 */
public interface OrderInfoService extends BaseShardingService<OrderInfo,OrderInfoMapper> {

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    OrderInfo getByOrderNo(String orderNo);

    /**
     *  订单填充用户昵称
     */
    void fillUsrname(List<OrderInfoVo> orderInfoVosr);
    
    /**
     * 批量删除
     * @param orderIds
     */
    Map<Integer,String> invalidBatch(Integer[] orderIds);

    /**
     * 分页查询
     */
    List<OrderInfoVo> findPageOrderInfo(@Param("orderInfoVo")OrderInfoVo orderInfoVo, @Param("pageAndSort")PageAndSort pageAndSort);

    /**
     * 保存二维码开票的订单
     */
    String addOrderByQrcode(String qrcodeItemName,String qrcodeItemPrice);

    OrderInfo getByOrderNo(String orderNo,Integer shardingId);

}
