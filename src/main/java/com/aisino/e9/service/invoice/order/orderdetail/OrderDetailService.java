package com.aisino.e9.service.invoice.order.orderdetail;

import com.aisino.core.exception.ErrorMessageException;
import com.aisino.core.service.BaseShardingService;
import com.aisino.e9.dao.invoice.order.orderdetail.OrderDetailMapper;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.service.BaseService;
import com.aisino.e9.entity.invoice.order.orderdetail.vo.OrderDetailVo;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by 为 on 2017-4-25.
 */
public interface OrderDetailService extends BaseShardingService<OrderDetail,OrderDetailMapper> {
    /**
     *  根据订单号分页查询
     */
    List<OrderDetail> findByOrderNo(String orderNo, PageAndSort pageAndSort);

    /**
     *  根据订单号查询
     */
    List<OrderDetail> findByOrderNo(String orderNo);

    /**
     * 导入并保存
     */
    void importAndSave(InputStream inputStream) throws OpenXML4JException, ParserConfigurationException, SAXException, IOException, ErrorMessageException;

    /**
     * 根据发票流水号查询明细
     */
    List<OrderDetail> findBySerialNo(String serialNo);

    /**
     * 根据订单号,查询是否存在已开票的明细,
     */
    boolean existAlreadyDetailByOrderNo(String orderNo);


    /**
     * 根据订单号,查询是否存在未开票票的明细,
     */
    boolean exisNotyetDetailByOrderNo(String orderNo);


    /**
     * 根据订单号查询 OrderDetailVo 分页
     */
    List<OrderDetailVo> findOrderDetailVoPage(String orderNo, PageAndSort pageAndSort);
}
