package com.aisino.base.invoice.order.orderinfo.service;

import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.impl.OrderInfoServiceImpl;
import com.aisino.core.service.BaseService;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-25.
 */
public interface OrderInfoService extends BaseService<OrderInfo,OrderInfoMapper> {

    /**
     *  从excel读取数据
     */
    OrderInfoServiceImpl.ImportResultDto importExcel(InputStream inputStream) throws IOException, OpenXML4JException,ParserConfigurationException, SAXException;

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    OrderInfo getByOrderNo(String orderNo);

    /**
     *  订单填充用户昵称
     */
    void fillUsrname(List<OrderInfo> orderInfos);
    
    /**
     * 批量删除
     * @param orderIds
     */
    Map<Integer,String> invalidBatch(Integer[] orderIds);

    /**
     * 根据导入的数据,插入数据库,目前还没做SQL合并优化
     * @param orderInfoCount
     * @param orderDetailCount
     * @param orderInfoMap
     * @param orderDetailMap
     * @return
     */
    OrderInfoServiceImpl.ImportResultDto insertToDb(int orderInfoCount, int orderDetailCount, Map<String, OrderInfo> orderInfoMap, Map<String, List<OrderDetail>> orderDetailMap);
}
