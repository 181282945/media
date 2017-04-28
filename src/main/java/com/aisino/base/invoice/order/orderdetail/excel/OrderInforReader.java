package com.aisino.base.invoice.order.orderdetail.excel;

import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.common.util.excel.reader.IRowReader;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 为 on 2017-4-28.
 */
public class OrderInforReader implements IRowReader {

    private List<OrderInfo> orderInfos = new ArrayList<>();

    private List<OrderDetail> orderDetails = new ArrayList<>();

    public List<OrderInfo> getOrderInfos(){
        return orderInfos;
    }
    public List<OrderDetail> getOrderDetails(){
        return orderDetails;
    }

    @Override
    public void getRows(int sheetIndex, int curRow, List<String> rowlist) {
        if (curRow == 0)
            return;
        if (sheetIndex == 0) {

            OrderInfo orderInfo = new OrderInfo();
            for (int k = 0; k < rowlist.size(); k++) {
                String value = rowlist.get(k);
                switch (k) {
                    case 0:
                        orderInfo.setTaxno(StringUtils.trimToNull(value));
                        break;
                    case 1:
//                    orderInfo.setDkflags(StringUtils.trimToNull(value));
                        break;
                    case 2:
                        orderInfo.setTicketCode(StringUtils.trimToNull(value));
                        break;
                    case 3:
                        orderInfo.setMajorItems(StringUtils.trimToNull(value));
                        break;
                    case 4:
                        orderInfo.setBuyerName(StringUtils.trimToNull(value));
                        break;
                    case 5:
                        orderInfo.setBuyerTaxno(StringUtils.trimToNull(value));
                        break;
                    case 6:
                        orderInfo.setBuyerAddr(StringUtils.trimToNull(value));
                        break;
                    case 7:
                        orderInfo.setBuyerProvince(StringUtils.trimToNull(value));
                        break;
                    case 8:
                        orderInfo.setBuyerTele(StringUtils.trimToNull(value));
                        break;
                    case 9:
                        orderInfo.setBuyerMobile(StringUtils.trimToNull(value));
                        break;
                    case 10:
                        orderInfo.setBuyerEmail(StringUtils.trimToNull(value));
                        break;
                    case 11:
                        orderInfo.setBuyerType(StringUtils.trimToNull(value));
                        break;
                    case 12:
                        orderInfo.setBuyerBankAcc(StringUtils.trimToNull(value));
                        break;
                    case 13:
                        orderInfo.setInduCode(StringUtils.trimToNull(value));
                        break;
                    case 14:
                        orderInfo.setInduName(StringUtils.trimToNull(value));
                        break;
                    case 15:
                        orderInfo.setRemarks(StringUtils.trimToNull(value));
                        break;
                    case 16:
                        orderInfo.setOrderNo(StringUtils.trimToNull(value));
                        break;
                    case 17:
                        orderInfo.setUsrno(StringUtils.trimToNull(value));
                        break;
                }
//                orderInfo.setTaxno(StringUtils.trimToNull(value));
//            } else if (1 == k) {
////                orderInfo.setDkflags(StringUtils.trimToNull(value));
//            } else if (2 == k) {
//                orderInfo.setTicketCode(StringUtils.trimToNull(value));
//            } else if (3 == k) {
//                orderInfo.setMajorItems(StringUtils.trimToNull(value));
//            } else if (4 == k) {
//                orderInfo.setBuyerName(StringUtils.trimToNull(value));
//            } else if (5 == k) {
//                orderInfo.setBuyerTaxno(StringUtils.trimToNull(value));
//            } else if (6 == k) {
//                orderInfo.setBuyerAddr(StringUtils.trimToNull(value));
//            } else if (7 == k) {
//                orderInfo.setBuyerProvince(StringUtils.trimToNull(value));
//            } else if (8 == k) {
//                orderInfo.setBuyerTele(StringUtils.trimToNull(value));
//            } else if (9 == k) {
//                orderInfo.setBuyerMobile(StringUtils.trimToNull(value));
//            } else if (10 == k) {
//                orderInfo.setBuyerEmail(StringUtils.trimToNull(value));
//            } else if (11 == k) {
//                orderInfo.setBuyerType(StringUtils.trimToNull(value));
//            } else if (12 == k) {
//                orderInfo.setBuyerBankAcc(StringUtils.trimToNull(value));
//            } else if (13 == k) {
//                orderInfo.setInduCode(StringUtils.trimToNull(value));
//            } else if (14 == k) {
//                orderInfo.setInduName(StringUtils.trimToNull(value));
//            } else if (15 == k) {
//                orderInfo.setRemarks(StringUtils.trimToNull(value));
//            } else if (16 == k) {
//                orderInfo.setOrderNo(StringUtils.trimToNull(value));
//            } else if (17 == k) {
//                orderInfo.setUsrno(StringUtils.trimToNull(value));
//            }

            }
            orderInfos.add(orderInfo);
        } else if (sheetIndex == 1) {
            OrderDetail orderDetail = new OrderDetail();
            for (int k = 0; k < rowlist.size(); k++) {
                String value = rowlist.get(k);
                switch (k) {
                    case 0:
                        orderDetail.setOrderNo(StringUtils.trimToNull(value));
                        break;
                    case 1:
                        orderDetail.setItemName(StringUtils.trimToNull(value));
                        break;
                    case 2:
                        orderDetail.setItemUnit(StringUtils.trimToNull(value));
                        break;
                    case 3:
                        orderDetail.setItemNum(StringUtils.trimToNull(value));
                        break;
                    case 4:
                        orderDetail.setSpecMode(StringUtils.trimToNull(value));
                        break;
                    case 5:
                        orderDetail.setItemPrice(StringUtils.trimToNull(value));
                        break;
                    case 6:
//                        orderDetail.setInvoiceNature(StringUtils.trimToNull(value));
                        break;
                    case 7:
                        orderDetail.setItemTaxCode(StringUtils.trimToNull(value));
                        break;
                }
                orderDetails.add(orderDetail);
            }
        }
    }
}
