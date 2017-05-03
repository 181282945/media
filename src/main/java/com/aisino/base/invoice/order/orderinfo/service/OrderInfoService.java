package com.aisino.base.invoice.order.orderinfo.service;

import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.core.service.BaseService;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 为 on 2017-4-25.
 */
public interface OrderInfoService extends BaseService<OrderInfo,OrderInfoMapper> {

    /**
     *  从excel读取数据
     */
    void importExcel(InputStream inputStream) throws IOException, OpenXML4JException,ParserConfigurationException, SAXException;
}
