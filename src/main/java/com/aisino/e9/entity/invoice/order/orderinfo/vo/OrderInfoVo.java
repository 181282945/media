package com.aisino.e9.entity.invoice.order.orderinfo.vo;

import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-7-4.
 */
@Alias("OrderInfoVo")
public class OrderInfoVo extends OrderInfo {


    /**
     * 用户昵称
     */
    private String usrName;

    /**
     * 企业名称
     */
    private String taxName;

    /**
     * 开票人,查询用,瞬时
     */
    private String drawer;

    //-------------------------getter and setter ---------------------------------------


    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }
}
