package com.aisino.e9.dao.invoice.invoiceinfo;

import com.aisino.core.dao.BaseShardingMapper;
import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.e9.entity.invoice.invoiceinfo.vo.InvoiceInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 为 on 2017-4-28.
 */
@Mapper
public interface InvoiceInfoMapper extends BaseShardingMapper<InvoiceInfo> {

    Long findPageInvoiceInfoCount(@Param("invoiceInfoVo")InvoiceInfoVo invoiceInfoVo);

    List<InvoiceInfoVo> findPageInvoiceInfo(@Param("invoiceInfoVo")InvoiceInfoVo InvoiceInfoVo, @Param("pageAndSort")PageAndSort pageAndSort);

}
