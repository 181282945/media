package com.aisino.e9.dao.invoice.order.orderdetail;

import com.aisino.core.dao.BaseShardingMapper;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderdetail.vo.OrderDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 为 on 2017-4-25.
 */
@Mapper
public interface OrderDetailMapper extends BaseShardingMapper<OrderDetail> {

    @Select("SELECT * FROM invoice_orderdetail WHERE serialNo = #{serialNo} AND shardingId = #{shardingId}")
    List<OrderDetail> findBySerialNo(@Param("serialNo") String serialNo,@Param("shardingId")Integer shardingId);

    /**
     * 根据订单号,查询是否存在已开票的明细,
     */
    @Select("SELECT count(0) FROM invoice_orderdetail a INNER JOIN invoice_invoiceinfo b WHERE a.serialNo = b.serialNo AND a.serialNo = b.serialNo AND a.orderNo = #{orderNo} AND b.status = 'A' AND a.shardingId = #{shardingId} LIMIT 1")
    long existAlreadyDetailByOrderNo(@Param("orderNo") String orderNo,@Param("shardingId")Integer shardingId);

    /**
     * 根据订单号,查询是否存在未开票票的明细,
     */
    @Select("SELECT count(0) FROM invoice_orderdetail a INNER JOIN invoice_invoiceinfo b WHERE a.serialNo = b.serialNo AND a.serialNo = b.serialNo AND a.orderNo = #{orderNo} AND b.status != 'A' AND a.shardingId = #{shardingId} LIMIT 1")
    long existNotyetDetailByOrderNo(@Param("orderNo") String orderNo,@Param("shardingId")Integer shardingId);

    /**
     * 根据订单号查询 OrderDetailVo 总数量
     */
    long findOrderDetailVoPageCount(@Param("orderNo") String orderNo);

    /**
     * 根据订单号查询 OrderDetailVo 分页
     */
    List<OrderDetailVo> findOrderDetailVoPage(@Param("orderNo") String orderNo, @Param("pageAndSort")PageAndSort pageAndSort);

}
