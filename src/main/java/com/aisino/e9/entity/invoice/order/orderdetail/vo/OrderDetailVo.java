package com.aisino.e9.entity.invoice.order.orderdetail.vo;

import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-6-27.
 */
@Alias("OrderDetailVo")
public class OrderDetailVo extends OrderDetail {

    /**
     * 开票状态
     */
    private String status;


    //----------------------getter and setter----------------------


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * 转义状态名称
     */
    public String getStatusName() {
        return InvoiceInfo.Status.getNameByCode(this.status);
    }
}
