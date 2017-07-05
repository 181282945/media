package com.aisino.e9.dao.invoice.order.orderinfo;

import com.aisino.core.dao.BaseShardingMapper;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.e9.entity.invoice.order.orderinfo.vo.OrderInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 为 on 2017-4-25.
 */
@Mapper
public interface OrderInfoMapper extends BaseShardingMapper<OrderInfo> {

    Long findPageOrderInfoCount(@Param("orderInfoVo")OrderInfoVo orderInfoVo);

    List<OrderInfoVo> findPageOrderInfo(@Param("orderInfoVo")OrderInfoVo orderInfoVo, @Param("pageAndSort")PageAndSort pageAndSort);
}
