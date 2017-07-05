package com.aisino.e9.dao.invoice.order.qrcodeorder;

import com.aisino.core.dao.BaseShardingMapper;
import com.aisino.e9.entity.invoice.order.qrcodeorder.pojo.QrcodeOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 为 on 2017-7-4.
 */
@Mapper
public interface QrcodeOrderMapper extends BaseShardingMapper<QrcodeOrder> {
}
