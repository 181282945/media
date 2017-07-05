package com.aisino.e9.service.invoice.invoiceinfo;

import com.aisino.core.service.BaseShardingService;
import com.aisino.e9.dao.invoice.invoiceinfo.InvoiceInfoMapper;
import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import com.aisino.e9.entity.invoice.invoiceinfo.vo.InvoiceInfoVo;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.invoiceinfo.impl.InvoiceInfoServiceImpl;
import com.aisino.common.model.xml.impl.KpRequestyl;
import com.aisino.common.model.xml.impl.Requestt;
import com.aisino.core.mybatis.specification.PageAndSort;

import java.util.List;


/**
 * Created by 为 on 2017-4-28.
 */
public interface InvoiceInfoService extends BaseShardingService<InvoiceInfo, InvoiceInfoMapper> {

    /**
     * 根据流水号查询发票信息
     *
     * @param serialNo
     * @return
     */
    InvoiceInfo getBySerialNo(String serialNo);

    /**
     * 请求开票接口并保存结果
     */
    boolean requestBilling(OrderInfo orderInfo, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType);

    /**
     *  拆分合并,自动开票
     */
    boolean autoBilling(String serialNo, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType);

    /**
     * 执行下载方法,并判断是否符合下载限制规则
     */
    InvoiceInfoServiceImpl.DownLoadHelper executeDownloadRequest(Integer invoiceId);

    /**
     * 发票信息填充昵称
     */
    void fillUsrname(List<InvoiceInfoVo> invoiceInfoVos);

    List<InvoiceInfoVo> findPageInvoiceInfo(InvoiceInfoVo invoiceInfoVo, PageAndSort pageAndSort);

    boolean billing(String orderNo,Requestt requestt, Integer invoiceId, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.RequestFpkjxx requestFpkjxx, InvoiceInfo.OperatingType operatingType);

    /**
     * 冲红
     */
    boolean doRed(InvoiceInfo invoiceInfo,KpRequestyl.FpkjxxFptxx.CzdmType czdmType, String reMarks);

    /**
     *  二维码开票
     */
    boolean qrcodeBilling(OrderInfo orderInfo);

}
