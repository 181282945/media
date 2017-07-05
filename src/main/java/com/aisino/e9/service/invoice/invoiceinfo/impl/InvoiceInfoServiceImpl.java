package com.aisino.e9.service.invoice.invoiceinfo.impl;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.authcodeinfo.params.RequestParams;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.core.mybatis.specification.ShardingSpecification;
import com.aisino.core.security.util.SecurityUtil;
import com.aisino.core.service.impl.BaseShardingServiceImpl;
import com.aisino.core.util.LocalError;
import com.aisino.e9.dao.invoice.invoiceinfo.InvoiceInfoMapper;
import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import com.aisino.e9.entity.invoice.invoiceinfo.vo.InvoiceInfoVo;
import com.aisino.e9.entity.invoice.order.dto.OrderBillingDto;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import com.aisino.e9.service.invoice.invoiceinfo.InvoiceInfoService;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.model.xml.impl.*;
import com.aisino.common.model.xml.util.XmlModelUtil;
import com.aisino.common.util.*;
import com.aisino.common.util.Base64;
import com.aisino.common.util.tax.TaxCalculationUtil;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.util.CloneUtils;
import com.aisino.core.util.DateUtil;
import com.aisino.core.util.SpringUtils;
import com.aisino.e9.service.rpc.requestt.RequesttService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by 为 on 2017-4-28.
 */
@Service("invoiceInfoService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class InvoiceInfoServiceImpl extends BaseShardingServiceImpl<InvoiceInfo, InvoiceInfoMapper> implements InvoiceInfoService {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private InvoiceInfoService invoiceInfoService;

    @Resource
    private IdWorker worker;

    @Resource
    private GlobalInfoParams globalInfoParams;

    @Resource
    private RequestParams requestParams;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RequesttService requesttService;

    @Override
    public InvoiceInfo getBySerialNo(String serialNo) {
        ShardingSpecification<InvoiceInfo> specification = new ShardingSpecification<>(InvoiceInfo.class, cuzSessionAttributes.getEnInfo().getShardingId());
        specification.addQueryLike(new QueryLike("serialNo", QueryLike.LikeMode.Eq, serialNo));
        return this.getOne(specification);
    }

    @Transactional(readOnly = true)
    @Override
    public void fillUsrname(List<InvoiceInfoVo> invoiceInfoVos) {
        for (InvoiceInfoVo invoiceInfoVo : invoiceInfoVos) {
            UserInfo userInfo = userInfoService.getUserByUsrno(invoiceInfoVo.getUsrno());
            if (userInfo != null)
                invoiceInfoVo.setUsrname(userInfo.getUsrname());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<InvoiceInfoVo> findPageInvoiceInfo(InvoiceInfoVo invoiceInfoVO, PageAndSort pageAndSort) {
        if (UserInfo.SubType.ADMIN.getCode().equals(cuzSessionAttributes.getUserInfo().getSubType())) {//判断是否管理员
            invoiceInfoVO.setTaxNo(cuzSessionAttributes.getEnInfo().getTaxno());
        } else {
            invoiceInfoVO.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());//如果不是的话,只能查询自己的
        }

        invoiceInfoVO.setShardingId(cuzSessionAttributes.getEnInfo().getShardingId());
        pageAndSort.setRowCount(getMapper().findPageInvoiceInfoCount(invoiceInfoVO));
        return getMapper().findPageInvoiceInfo(invoiceInfoVO, pageAndSort);
    }

    /**
     * 子类可以重写新增验证方法
     */
    @Override
    protected void validateAddEntity(InvoiceInfo entity) {
        super.validateAddEntity(entity);
        if (StringUtils.trimToNull(entity.getUsrno()) == null) {
            if (cuzSessionAttributes.getUserInfo() != null && cuzSessionAttributes.getUserInfo().getUsrno() != null)
                entity.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());
            else
                entity.setUsrno(SecurityUtil.ANONYMOUSUSER);
        }

        if (StringUtils.trimToNull(entity.getStatus()) == null)
            entity.setStatus(InvoiceInfo.Status.NOTYET.getCode());
    }

    @Override
    public DownLoadHelper executeDownloadRequest(Integer invoiceId) {
        InvoiceInfo invoiceInfo = invoiceInfoService.findEntityById(invoiceId, cuzSessionAttributes.getEnInfo().getShardingId());
        //如果最后一次下不是今天,那么更新下次次数为0
        if (!DateUtil.isToday(invoiceInfo.getInsertDate())) {
            invoiceInfo.setDownloadCount(0);
            invoiceInfoService.updateEntity(invoiceInfo, cuzSessionAttributes.getEnInfo().getShardingId());
        }
        DownLoadHelper downLoadHelper = new DownLoadHelper();
        downLoadHelper.setInvoiceInfo(invoiceInfo);
        //如果最后一次下载日期是今天,那么判断今天下载次数,并限制下载间隔
        if (DateUtil.isToday(invoiceInfo.getInsertDate())) {
            if (invoiceInfo.getDownloadCount().intValue() >= 3) {
                downLoadHelper.setSuccess(false);
                downLoadHelper.setErrorMsg("此票今天下载次数已达上限,请明天再试!(每张票每天最多只能下载3次)");
                return downLoadHelper;
            }
            long interval = DateUtil.newDate().getTime() - invoiceInfo.getInsertDate().getTime();
            if (interval < 5000) {
                downLoadHelper.setSuccess(false);
                downLoadHelper.setErrorMsg("请" + ((5000 - interval) / 1000) + "秒后再试!");
                return downLoadHelper;
            }
        }


        try {
            URL url = new URL(invoiceInfo.getPdfUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpStatus.OK.value()) {
                conn.disconnect();
                downLoadHelper.setSuccess(true);
                return downLoadHelper;
            } else {
                return this.downloadRequest(downLoadHelper);
            }
        } catch (MalformedURLException e) {
            return this.downloadRequest(downLoadHelper);
        } catch (IOException e) {
            return this.downloadRequest(downLoadHelper);
        }
    }

    private DownLoadHelper downloadRequest(DownLoadHelper downLoadHelper) {
        XzRequest xzRequest = new XzRequest(downLoadHelper.getInvoiceInfo().getSerialNo(), cuzSessionAttributes.getAuthCodeInfo().getPlatformCode(), cuzSessionAttributes.getAuthCodeInfo().getTaxNo(), null);
        String xmlContent = XmlModelUtil.beanToXmlStr(xzRequest, XzRequest.class);
        Requestt requestt = requesttService.careateRequestt(globalInfoParams.getDownloadInterfaceCode());
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));
        String info = XmlModelUtil.doPost(requestt, Requestt.class, requestParams.getUrl());
        Requestt result = XmlModelUtil.xmlStrToBean(info, Requestt.class);

        downLoadHelper.setInvoiceInfo(downLoadHelper.getInvoiceInfo());
        if (Requestt.SUCCESS_CODE.equals(result.getReturnStateInfo().getReturnCode())) {
            ResponseFpxxxz responseFpxxxz = XmlModelUtil.xmlStrToBean(AesUtil.decrypt(result.getData().getContent()), ResponseFpxxxz.class);
            downLoadHelper.setSuccess(true);
            downLoadHelper.getInvoiceInfo().setPdfUrl(responseFpxxxz.getPdfUrl());
            return downLoadHelper;
        }

        downLoadHelper.setSuccess(false);
        downLoadHelper.setErrorMsg(Base64.getFromBase64(result.getReturnStateInfo().getReturnMessage()));
        return downLoadHelper;
    }


    /**
     * 开票
     */
    @Override
    public boolean requestBilling(OrderInfo orderInfo, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(orderInfo.getOrderNo());
        OrderBillingDto orderBillingDto = new OrderBillingDto(orderInfo);
        Set<String> orderNoSet = new HashSet<>();

        //排除已经关联发票流水号的明细
        Iterator<OrderDetail> orderDetailIterator = orderDetails.iterator();
        while (orderDetailIterator.hasNext()) {
            OrderDetail orderDetail = orderDetailIterator.next();
            if (StringUtils.trimToNull(orderDetail.getSerialNo()) != null) {
                orderDetailIterator.remove();
            } else {
                orderNoSet.add(orderDetail.getOrderNo());
            }
        }
        if (orderDetails.isEmpty()) {
            LocalError.getMapMessage().get().put(orderInfo.getOrderNo(), orderInfo.getOrderNo() + ":如果此订单明细不为空,那么已经被拆分或合并到不同发票中,不能进行手动开票!");
            return false;
        }

        //如果是二维码开票,那么不关联流水号
        if(!OrderInfo.Type.QRCODE.getCode().equals(orderInfo.getType())){
            ((InvoiceInfoServiceImpl) AopContext.currentProxy()).checkAndSet(orderDetails);
        }

        orderBillingDto.setSerialNo(orderDetails.get(0).getSerialNo());
        KpRequestyl.RequestFpkjxx requestFpkjxx = this.createKpRequestyl(orderBillingDto, orderDetails, invoiceType, czdmType, null);
        if (requestFpkjxx == null)
            return false;
        Requestt requestt = requesttService.careateRequestt(globalInfoParams.getBillingInterfaceCode());
        String xmlContent = XmlModelUtil.beanToXmlStr(requestFpkjxx, KpRequestyl.RequestFpkjxx.class);
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));

        boolean resultFlag = ((InvoiceInfoService) SpringUtils.getBean("invoiceInfoService")).billing(orderInfo.getOrderNo(), requestt, null, InvoiceInfo.InvoiceType.NORMAL, requestFpkjxx, InvoiceInfo.OperatingType.GENERAL);

        /**
         * 如果所有明细已经开票,那么更新主档状态
         */
        for (String orderNo : orderNoSet) {
            if (!orderDetailService.exisNotyetDetailByOrderNo(orderNo)) {
                orderInfoService.updateEntityStatus(orderInfoService.getByOrderNo(orderNo).getId(), OrderInfo.StatusType.ALREADY.getCode(), cuzSessionAttributes.getEnInfo().getShardingId());
            }
        }
        return resultFlag;
    }


    /**
     * 二维码开票
     */
    @Override
    public boolean qrcodeBilling(OrderInfo orderInfo) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(orderInfo.getOrderNo());
        OrderBillingDto orderBillingDto = new OrderBillingDto(orderInfo);
        if(StringUtils.trimToNull(orderDetails.get(0).getSerialNo())==null)
            orderDetails.get(0).setSerialNo(String.valueOf(worker.nextId()));

        orderBillingDto.setSerialNo(orderDetails.get(0).getSerialNo());
        KpRequestyl.RequestFpkjxx requestFpkjxx = this.createKpRequestyl(orderBillingDto, orderDetails, InvoiceInfo.InvoiceType.NORMAL,KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL, null);
        if (requestFpkjxx == null)
            return false;
        Requestt requestt = requesttService.careateRequestt(globalInfoParams.getBillingInterfaceCode());
        String xmlContent = XmlModelUtil.beanToXmlStr(requestFpkjxx, KpRequestyl.RequestFpkjxx.class);
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));

        boolean resultFlag = ((InvoiceInfoService) SpringUtils.getBean("invoiceInfoService")).billing(orderInfo.getOrderNo(), requestt, null, InvoiceInfo.InvoiceType.NORMAL, requestFpkjxx, InvoiceInfo.OperatingType.GENERAL);

        /**
         * 如果所有明细已经开票,那么更新主档状态
         */
        if(resultFlag){
            orderInfoService.updateEntityStatus(orderInfo.getId(), OrderInfo.StatusType.ALREADY.getCode(), cuzSessionAttributes.getEnInfo().getShardingId());
        }
        return resultFlag;
    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndSet(List<OrderDetail> orderDetails) {
        if (StringUtils.trimToNull(orderDetails.get(0).getSerialNo()) == null) {
            String serialNo = String.valueOf(worker.nextId());
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.setSerialNo(serialNo);
                orderDetail.setSplitMergeMark(OrderDetail.SplitMergeMark.RAW.getCode());
                orderDetailService.updateEntity(orderDetail, cuzSessionAttributes.getEnInfo().getShardingId());
            }
            InvoiceInfo invoiceInfo = new InvoiceInfo();
            invoiceInfo.setTaxNo(cuzSessionAttributes.getEnInfo().getTaxno());
            invoiceInfo.setSerialNo(serialNo);
            invoiceInfo.setShardingId(cuzSessionAttributes.getEnInfo().getShardingId());
            invoiceInfoService.addEntity(invoiceInfo);//并保存临时发票
        }
    }


    /**
     * 自动开票
     */
    @Override
    public boolean autoBilling(String serialNo, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType) {
        List<OrderDetail> orderDetails = orderDetailService.findBySerialNo(serialNo);
        OrderInfo orderInfo = orderInfoService.getByOrderNo(orderDetails.get(0).getOrderNo());
        OrderBillingDto orderBillingDto = new OrderBillingDto(orderInfo);
        orderBillingDto.setSerialNo(serialNo);

        Set<String> orderNoSet = new HashSet<>();
        for (OrderDetail orderDetail : orderDetails) {
            orderNoSet.add(orderDetail.getOrderNo());
        }

        KpRequestyl.RequestFpkjxx requestFpkjxx = this.createKpRequestyl(orderBillingDto, orderDetails, invoiceType, czdmType, null);

        InvoiceInfo invoiceInfo = this.getBySerialNo(serialNo);
        //更新一次发票信息,如果开票成功,再更新一次
        ((InvoiceInfoServiceImpl) AopContext.currentProxy()).updateInvoice(invoiceInfo, invoiceType, requestFpkjxx);
        if (requestFpkjxx == null)
            return false;
        Requestt requestt = requesttService.careateRequestt(globalInfoParams.getBillingInterfaceCode());
        String xmlContent = XmlModelUtil.beanToXmlStr(requestFpkjxx, KpRequestyl.RequestFpkjxx.class);
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));

        boolean resultFalg = ((InvoiceInfoService) SpringUtils.getBean("invoiceInfoService")).billing(orderInfo.getOrderNo(), requestt, invoiceInfo.getId(), InvoiceInfo.InvoiceType.NORMAL, requestFpkjxx, InvoiceInfo.OperatingType.SIMPLE);

        /**
         * 如果所有明细已经开票,那么更新主档状态
         */
        for (String orderNo : orderNoSet) {
            if (!orderDetailService.exisNotyetDetailByOrderNo(orderNo)) {
                orderInfoService.updateEntityStatus(orderInfoService.getByOrderNo(orderNo).getId(), OrderInfo.StatusType.ALREADY.getCode(), cuzSessionAttributes.getEnInfo().getShardingId());
            }
        }
        return resultFalg;
    }


    /**
     * 冲红
     */
    @Override
    public boolean doRed(InvoiceInfo invoiceInfo, KpRequestyl.FpkjxxFptxx.CzdmType czdmType, String reMarks) {
        List<OrderDetail> orderDetails = orderDetailService.findBySerialNo(invoiceInfo.getSerialNo());
        OrderInfo orderInfo = orderInfoService.getByOrderNo(orderDetails.get(0).getOrderNo());
        OrderBillingDto orderBillingDto = new OrderBillingDto(orderInfo);
        orderBillingDto.setSerialNo(invoiceInfo.getSerialNo());
        KpRequestyl.RequestFpkjxx requestFpkjxx = this.createKpRequestyl(orderBillingDto, orderDetails, InvoiceInfo.InvoiceType.RED, czdmType, reMarks);
        Requestt requestt = requesttService.careateRequestt(globalInfoParams.getBillingInterfaceCode());
        String xmlContent = XmlModelUtil.beanToXmlStr(requestFpkjxx, KpRequestyl.RequestFpkjxx.class);
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));
        return ((InvoiceInfoService) SpringUtils.getBean("invoiceInfoService")).billing(orderInfo.getOrderNo(), requestt, null, InvoiceInfo.InvoiceType.RED, requestFpkjxx, InvoiceInfo.OperatingType.GENERAL);
    }

//    /**
//     * 开票
//     */
//    @Override
//    public Integer billing(Requestt requestt, Integer invoiceId, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.RequestFpkjxx requestFpkjxx, InvoiceInfo.OperatingType operatingType) {
//
//        String info = XmlModelUtil.doPost(requestt, Requestt.class, requestParams.getUrl());
//
//        Requestt result = XmlModelUtil.xmlStrToBean(info, Requestt.class);
//        //解析返回信息
//        String returnCode = result.getReturnStateInfo().getReturnCode();
//
//        //如果成功返回信息,保存发票信息
//        if (Requestt.SUCCESS_CODE.equals(returnCode)) {
//            String decrptContent = AesUtil.decrypt(result.getData().getContent());
//            if (InvoiceInfo.OperatingType.GENERAL.equals(operatingType)) {//普通操作
//                return this.addByResponseFpkj(decrptContent, invoiceType, requestFpkjxx);
//            } else {//拆分合并操作,在没成功已经已经保存了发票信息,成功之后,这里需要更新
//                this.updateByResponseFpkj(decrptContent, invoiceId);
//            }
//        } else {
//            throw new RuntimeException(Base64.getFromBase64(result.getReturnStateInfo().getReturnMessage()));
//        }
//        return null;
//    }

    /**
     * 开票
     */
    @Override
    public boolean billing(String orderNo, Requestt requestt, Integer invoiceId, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.RequestFpkjxx requestFpkjxx, InvoiceInfo.OperatingType operatingType) {

        String info = XmlModelUtil.doPost(requestt, Requestt.class, requestParams.getUrl());

        Requestt result = XmlModelUtil.xmlStrToBean(info, Requestt.class);
        //解析返回信息
        String returnCode = result.getReturnStateInfo().getReturnCode();

        //如果成功返回信息,保存发票信息
        if (Requestt.SUCCESS_CODE.equals(returnCode)) {
            String decrptContent = AesUtil.decrypt(result.getData().getContent());
            if (InvoiceInfo.OperatingType.GENERAL.equals(operatingType)) {//普通操作
                ((InvoiceInfoServiceImpl) AopContext.currentProxy()).addByResponseFpkj(decrptContent, invoiceType, requestFpkjxx);
            } else {
                ((InvoiceInfoServiceImpl) AopContext.currentProxy()).updateByResponseFpkj(decrptContent, invoiceId, InvoiceInfo.Status.ALREADY);
            }
            return true;
        } else {
            if (InvoiceInfo.OperatingType.SIMPLE.equals(operatingType)) {//拆分合并
                ((InvoiceInfoServiceImpl) AopContext.currentProxy()).updateByResponseFpkj(null, invoiceId, InvoiceInfo.Status.PENDING);
            }
            LocalError.getMapMessage().get().put(orderNo, Base64.getFromBase64(result.getReturnStateInfo().getReturnMessage()));
            return false;
        }
    }

    /**
     * 拆分合并时,要先保存发票
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateInvoice(InvoiceInfo invoiceInfo, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.RequestFpkjxx requestFpkjxx) {
        invoiceInfo.setSerialNo(requestFpkjxx.getFpkjxxFptxx().getFpqqlsh());
        invoiceInfo.setProjectName(requestFpkjxx.getFpkjxxFptxx().getKpxm());
        invoiceInfo.setTaxNo(cuzSessionAttributes.getEnInfo().getTaxno());
        invoiceInfo.setPriceTotal(requestFpkjxx.getFpkjxxFptxx().getKphjje());
        invoiceInfo.setStatus(InvoiceInfo.Status.PENDING.getCode());
        invoiceInfo.setInvoiceType(invoiceType.getCode());
        invoiceInfo.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());
        this.updateEntity(invoiceInfo, cuzSessionAttributes.getEnInfo().getShardingId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateByResponseFpkj(String decrptContent, Integer invoiceId, InvoiceInfo.Status status) {
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setId(invoiceId);
        if (decrptContent != null) {
            ResponseFpkj responseFpkj = XmlModelUtil.xmlStrToBean(decrptContent, ResponseFpkj.class);
            invoiceInfo.setNoTaxTotal(responseFpkj.getHjbhsje());
            invoiceInfo.setTaxTotal(responseFpkj.getHjse());
            invoiceInfo.setInvoiceDate(DateUtil.parseDate(DateUtil.KPRQ_PATTON, responseFpkj.getKprq()));
            invoiceInfo.setPdfUrl(responseFpkj.getPdfurl());
            invoiceInfo.setInvoiceCode(responseFpkj.getFpdm());
            invoiceInfo.setInvoiceNum(responseFpkj.getFphm());
        }
        invoiceInfo.setStatus(status.getCode());
        this.updateEntity(invoiceInfo, cuzSessionAttributes.getEnInfo().getShardingId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addByResponseFpkj(String decrptContent, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.RequestFpkjxx requestFpkjxx) {
        ResponseFpkj responseFpkj = XmlModelUtil.xmlStrToBean(decrptContent, ResponseFpkj.class);
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setTaxNo(cuzSessionAttributes.getEnInfo().getTaxno());
        invoiceInfo.setSerialNo(requestFpkjxx.getFpkjxxFptxx().getFpqqlsh());
        invoiceInfo.setProjectName(requestFpkjxx.getFpkjxxFptxx().getKpxm());
        invoiceInfo.setNoTaxTotal(responseFpkj.getHjbhsje());
        invoiceInfo.setPriceTotal(requestFpkjxx.getFpkjxxFptxx().getKphjje());
        invoiceInfo.setTaxTotal(responseFpkj.getHjse());
        invoiceInfo.setInvoiceDate(DateUtil.parseDate(DateUtil.KPRQ_PATTON, responseFpkj.getKprq()));
        invoiceInfo.setStatus(InvoiceInfo.Status.ALREADY.getCode());
        invoiceInfo.setPdfUrl(responseFpkj.getPdfurl());
        invoiceInfo.setInvoiceType(invoiceType.getCode());

        if(cuzSessionAttributes.getUserInfo()!=null&&cuzSessionAttributes.getUserInfo().getUsrno()!=null)
            invoiceInfo.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());
        else
            invoiceInfo.setUsrno(SecurityUtil.ANONYMOUSUSER);

        invoiceInfo.setInvoiceCode(responseFpkj.getFpdm());
        invoiceInfo.setInvoiceNum(responseFpkj.getFphm());
        this.addEntity(invoiceInfo);
    }


    /**
     * @return
     */
    public KpRequestyl.RequestFpkjxx createKpRequestyl(OrderBillingDto orderBillingDto, List<OrderDetail> orderDetails, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType, String reMarks) {

        InvoiceInfo orginalInvoiceInfo = invoiceInfoService.getBySerialNo(orderDetails.get(0).getSerialNo());

        //如果购货方是企业,那么必须要有企业税号
        if (OrderInfo.BuyerType.ENTERPRISE.getCode().equals(orderBillingDto.getBuyerType()) || OrderInfo.BuyerType.INSTITUTIONS.getCode().equals(orderBillingDto.getBuyerType())) {
            if (orderBillingDto.getBuyerTaxno() == null) {
                LocalError.getMapMessage().get().put(orderBillingDto.getSerialNo(), "发票流水号:" + orderBillingDto.getSerialNo() + "购货方为企业/机关事业单位,缺少购货方企业税号!");
                return null;
            }
        }
        //蓝票
//        if (InvoiceInfo.InvoiceType.NORMAL.equals(invoiceType)) {

//            //如果是开蓝票,判断是否已经开过票
//            if (OrderInfo.StatusType.ALREADY.getCode().equals(orderInfo.getStatus()) && KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL.equals(czdmType)) {
//                map.put(orderInfo.getOrderNo(), "单号:" + orderInfo.getOrderNo() + "此订单已经开票,不能重复开票!");
//                return null;
//            }
//            //如果开蓝票,那么设置发票唯一流水号
//            if (StringUtils.isBlank(orderDetails.get(0).getSerialNo())) {
//                orderInfo.setSerialNo(String.valueOf(worker.nextId()));//设置发票唯一流水号
//                orderInfoService.updateEntity(orderInfo);
//            }
//        }
        //红票
        if (InvoiceInfo.InvoiceType.RED.equals(invoiceType)) {
            orginalInvoiceInfo = invoiceInfoService.getBySerialNo(orderBillingDto.getSerialNo());
            if (orginalInvoiceInfo == null) {
                LocalError.getMapMessage().get().put(orderBillingDto.getSerialNo(), "没有找到原发票信息,冲红失败!");
                return null;
            }

            if (InvoiceInfo.RedflagsType.ALREADY.getCode().equals(orginalInvoiceInfo.getRedflags())) {
                LocalError.getMapMessage().get().put(orderBillingDto.getSerialNo(), "发票代码:" + orginalInvoiceInfo.getInvoiceCode() + "此发票不能重复冲红!");
                return null;
            }

            if (orginalInvoiceInfo.getSerialNoRed() == null) {
                orginalInvoiceInfo.setSerialNoRed(String.valueOf(worker.nextId()));//设置红票唯一流水号
                ((InvoiceInfoServiceImpl) AopContext.currentProxy()).commitUpdateInvoiceInfo(orginalInvoiceInfo);
            }
        }

        final EnInfo enInfo = cuzSessionAttributes.getEnInfo();
        final AuthCodeInfo authCodeInfo = cuzSessionAttributes.getAuthCodeInfo();

        KpRequestyl.RequestFpkjxx requestFpkjxx = new KpRequestyl.RequestFpkjxx();

        //项目明细
        KpRequestyl.FpkjxxXmxxs fpkjxxXmxxs = new KpRequestyl.FpkjxxXmxxs();

        //订单明细
        KpRequestyl.FpkjxxDdmxxxs fpkjxxDdmxxxs = new KpRequestyl.FpkjxxDdmxxxs();

        Map<Integer, List<OrderDetail>> orderDetailMap = new HashMap<>();
        Integer[] invoiceNatureCode = new Integer[OrderDetail.InvoiceNature.values().length];

        /**
         * 根据发票行性质分组
         */
        for (int i = 0; i < OrderDetail.InvoiceNature.values().length; i++) {
            invoiceNatureCode[i] = OrderDetail.InvoiceNature.values()[i].getCode();
            orderDetailMap.put(invoiceNatureCode[i], new ArrayList<OrderDetail>());
        }

        for (OrderDetail orderDetail : orderDetails) {
            orderDetailMap.get(orderDetail.getInvoiceNature()).add(orderDetail);
        }

        //判断折扣行与被折扣行是否匹配
        if (orderDetailMap.get(OrderDetail.InvoiceNature.DISCOUNT.getCode()).size() != orderDetailMap.get(OrderDetail.InvoiceNature.DISCOUNTLINE.getCode()).size()) {
            LocalError.getMapMessage().get().put(orderBillingDto.getSerialNo(), "发票流水号:" + orderBillingDto.getSerialNo() + "折扣行与被折扣行数量不匹配,开票失败!");
            return null;
        }


        List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList = new ArrayList<>(orderDetails.size());
        List<KpRequestyl.FpkjxxDdmxxx> fpkjxxDdmxxxList = new ArrayList<>();


        //开正票
        if (InvoiceInfo.InvoiceType.NORMAL.equals(invoiceType))
            //先构建 折扣行与被折扣行
            fillXmxxDdmxxx(enInfo, fpkjxxXmxxList, fpkjxxDdmxxxList, orderDetailMap);
        //开红票
        if (InvoiceInfo.InvoiceType.RED.equals(invoiceType))
            //先构建 折扣行与被折扣行
            fillXmxxDdmxxxRed(enInfo, fpkjxxXmxxList, fpkjxxDdmxxxList, orderDetailMap);

        //如果此订单错误信息不为null 马上返回空
        if (LocalError.getMapMessage().get().get(orderBillingDto.getSerialNo()) != null)
            return null;


        fpkjxxXmxxs.setSize(fpkjxxXmxxList.size() + "");
        fpkjxxXmxxs.setFpkjxxXmxxList(fpkjxxXmxxList);

        requestFpkjxx.setFpkjxxXmxxs(fpkjxxXmxxs);

        fpkjxxDdmxxxs.setSize(fpkjxxDdmxxxList.size() + "");
        fpkjxxDdmxxxs.setFpkjxxDdmxxxList(fpkjxxDdmxxxList);

        requestFpkjxx.setFpkjxxDdmxxxs(fpkjxxDdmxxxs);


        //项目信息
        KpRequestyl.FpkjxxDdxx fpkjxxDdxx = new KpRequestyl.FpkjxxDdxx();
        fpkjxxDdxx.setDdh(orderBillingDto.getOrderNo());   //订单号
//        fpkjxxDdxx.setThdh();  //退货单号
        if (orderBillingDto.getOrderDate() != null)
            fpkjxxDdxx.setDddate(DateFormatUtils.format(orderBillingDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss SS"));  //订单日期

        requestFpkjxx.setFpkjxxDdxx(fpkjxxDdxx);


        //支付信息 param:支付方式,支付流水号,支付平台
        KpRequestyl.FpkjxxZfxx fpkjxxZfxx = new KpRequestyl.FpkjxxZfxx(null, null, null);

        requestFpkjxx.setFpkjxxZfxx(fpkjxxZfxx);

        //物流信息 param:承运公司,送货时间,物流单号,送货地址
        KpRequestyl.FpkjxxWlxx fpkjxxWlxx = new KpRequestyl.FpkjxxWlxx(null, null, null, null);


        requestFpkjxx.setFpkjxxWlxx(fpkjxxWlxx);

        KpRequestyl.FpkjxxFptxx fpkjxxFptxx = createFpkjxxFptxx(authCodeInfo, enInfo, orderDetails.get(0).getSerialNo(), orderBillingDto, orginalInvoiceInfo, fpkjxxXmxxList, invoiceType, czdmType, reMarks);
        requestFpkjxx.setFpkjxxFptxx(fpkjxxFptxx);
        return requestFpkjxx;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void commitUpdateInvoiceInfo(InvoiceInfo orginalInvoiceInfo) {
        invoiceInfoService.updateEntity(orginalInvoiceInfo, cuzSessionAttributes.getEnInfo().getShardingId());
    }

    /**
     * 创建请求报文
     */
    private KpRequestyl.FpkjxxFptxx createFpkjxxFptxx(AuthCodeInfo authCodeInfo, EnInfo enInfo, String serialNo, OrderBillingDto orderBillingDto, InvoiceInfo orginalInvoiceInfo, List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType, String reMarks) {
        KpRequestyl.FpkjxxFptxx fpkjxxFptxx = new KpRequestyl.FpkjxxFptxx();
        fpkjxxFptxx.setFpqqlsh(serialNo);//发票请求唯一流水号
        fpkjxxFptxx.setDsptbm(authCodeInfo.getPlatformCode());//平台编码
        fpkjxxFptxx.setNsrsbh(enInfo.getTaxno());//开票方识别号
        fpkjxxFptxx.setNsrmc(enInfo.getTaxname());//开票方名称
        fpkjxxFptxx.setNsrdzdah(authCodeInfo.getElecNmber()); //开票方电子档案号
        fpkjxxFptxx.setSwjgdm(authCodeInfo.getTaxCode()); //税务机构代码
        fpkjxxFptxx.setDkbz(orderBillingDto.getDkflags().toString()); //代开标志
        fpkjxxFptxx.setPydm(orderBillingDto.getTicketCode()); //票样代码
        fpkjxxFptxx.setKpxm(orderBillingDto.getMajorItems());   //主要开票项目
        fpkjxxFptxx.setBmbbbh(authCodeInfo.getCodeTableVer());  //编码表版本号
        fpkjxxFptxx.setXhfnsrsbh(enInfo.getTaxno()); //销货方识别号
        fpkjxxFptxx.setXhfmc(enInfo.getTaxname());   //销货方名称
        fpkjxxFptxx.setXhfdz(enInfo.getTaxaddr());  //销货方地址
        fpkjxxFptxx.setXhfdh(enInfo.getTaxtel());  //销货方电话
        fpkjxxFptxx.setXhfyhzh(enInfo.getBankaccount());  //销货方银行账号
        fpkjxxFptxx.setGhfmc(orderBillingDto.getBuyerName());  //购货方名称
        fpkjxxFptxx.setGhfnsrsbh(orderBillingDto.getBuyerTaxno());    //购货方识别号
        fpkjxxFptxx.setGhfsf(orderBillingDto.getBuyerProvince());  //购货方省份
        fpkjxxFptxx.setGhfdz(orderBillingDto.getBuyerAddr());  //购货方地址
        fpkjxxFptxx.setGhfgddh(orderBillingDto.getBuyerTele());  //购货方固定电话
        fpkjxxFptxx.setGhfsj(orderBillingDto.getBuyerMobile());  //购货方手机
        fpkjxxFptxx.setGhfemail(orderBillingDto.getBuyerEmail());  //购货方邮箱
        fpkjxxFptxx.setGhfqylx(orderBillingDto.getBuyerType());  //购货方企业类型
        fpkjxxFptxx.setGhfyhzh(orderBillingDto.getBuyerBankAcc());  //购货方银行账号
        fpkjxxFptxx.setHydm(orderBillingDto.getInduCode());  //行业代码
        fpkjxxFptxx.setHymc(orderBillingDto.getInduName());    //行业名称
        fpkjxxFptxx.setKpy(enInfo.getDrawer());  //开票员
        fpkjxxFptxx.setSky(enInfo.getPayee());  //收款员
        fpkjxxFptxx.setFhr(enInfo.getReviewer());  //复核人
//        fpkjxxFptxx.setKprq();  //开票日期  系统生成不用写
        fpkjxxFptxx.setKplx(invoiceType.getCode());  //开票类型    1 正票 2 红票

        if (InvoiceInfo.InvoiceType.RED.equals(invoiceType)) {
            fpkjxxFptxx.setFpqqlsh(orginalInvoiceInfo.getSerialNoRed());//发票请求唯一流水号
            fpkjxxFptxx.setYfpdm(orginalInvoiceInfo.getInvoiceCode());  //原发票代码
            fpkjxxFptxx.setYfphm(orginalInvoiceInfo.getInvoiceNum());  //原发票号码
            /**
             *   10 正票正常开具
             *   11 正票错票重开
             *   20 退货折让红票、
             *   21 错票重开红票、
             *   22 换票冲红（全冲
             *   红电子发票，开具
             *   纸质发票）
             */
            fpkjxxFptxx.setCzdm(czdmType.getCode());  //操作代码
            /**
             * 0：根据项目名称字数，自动产生清单， 保持目前逻辑不变     1：取清单对应票面内容字段打印到发信息 XMXX 打印到清  单上。默认为 0。
             */
            fpkjxxFptxx.setQdbz(orginalInvoiceInfo.getListFlag());  //清单标志
            /**
             * 需要打印清单时对 应发票票面项目名 称 清单标识（ QD_BZ） 为 1 时必填。 为 0 不进行   处理
             */
            if (InvoiceInfo.ListFlagType.LIST.getCode().equals(fpkjxxFptxx.getQdbz()))
                fpkjxxFptxx.setQdxmmc(orginalInvoiceInfo.getListItemName());  //清单发票项目名称

            if (InvoiceInfo.InvoiceType.RED.equals(invoiceType)) {
                fpkjxxFptxx.setChyy(reMarks);  //冲红原因 冲红时填写，由企 业定义
//        fpkjxxFptxx.setTschbz();  //特殊冲红标志
            }

        }


//        fpkjxxFptxx.setBz();  //备注
//        fpkjxxFptxx.setByzd1();  //备用字段1
//        fpkjxxFptxx.setByzd2();  //备用字段2
//        fpkjxxFptxx.setByzd3();  //备用字段3
//        fpkjxxFptxx.setByzd4();  //备用字段4
//        fpkjxxFptxx.setByzd5();  //备用字段5


        //不含税金额合计
        BigDecimal bhsjehj = new BigDecimal(0);
        //含税金额合计
        BigDecimal hsjehj = new BigDecimal(0);
        //合计税额
        BigDecimal hjse = new BigDecimal(0);

        for (KpRequestyl.FpkjxxXmxx fpkjxxXmxx : fpkjxxXmxxList) {
            BigDecimal se = new BigDecimal(fpkjxxXmxx.getSe());
            hjse = hjse.add(se);
            if (OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode().equals(fpkjxxXmxx.getHsbz())) {
                hsjehj = hsjehj.add(new BigDecimal(fpkjxxXmxx.getXmje()).add(se));
                bhsjehj = bhsjehj.add(new BigDecimal(fpkjxxXmxx.getXmje()));
            }

            if (OrderDetail.TaxIncludedFlag.INCLUDE.getCode().equals(fpkjxxXmxx.getHsbz())) {
                hsjehj = hsjehj.add(new BigDecimal(fpkjxxXmxx.getXmje()));
                bhsjehj = bhsjehj.add(new BigDecimal(fpkjxxXmxx.getXmje()).subtract(se));
            }
//            hjse = hjse.add(new BigDecimal(fpkjxxXmxx.getSe()));
//            hsjehj = hsjehj.add(new BigDecimal(fpkjxxXmxx.getXmje()));
//            bhsjehj = bhsjehj.add(new BigDecimal(TaxCalculationUtil.calcBHSJE(fpkjxxXmxx.getSl(), fpkjxxXmxx.getXmje())));
        }

        fpkjxxFptxx.setHjbhsje(bhsjehj.setScale(2, BigDecimal.ROUND_HALF_UP).toString());  //合计不含税金额 小数点后 2 位，以     元为单位精确到分（单行商品金额之   和）。 平台处理价税        分离，此值传 0
        fpkjxxFptxx.setKphjje(hsjehj.setScale(2, BigDecimal.ROUND_HALF_UP).toString());  //价税合计金额 小数点后 元为单位精确到分 2 位，
        fpkjxxFptxx.setHjse(hjse.setScale(2, BigDecimal.ROUND_HALF_UP).toString());  //合计税额 小数点后 2 位，以     元为单位精确到分         (单行商品税额之    和)，平台处理价税      分离，此值传 0

        return fpkjxxFptxx;
    }


    /**
     * 只处理蓝票 构建请求报文的项目明细,发票明细,订单明细信息,处理折扣行
     *
     * @param fpkjxxXmxxList
     * @param fpkjxxDdmxxxList
     */
    private void fillXmxxDdmxxx(EnInfo enInfo, List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList, List<KpRequestyl.FpkjxxDdmxxx> fpkjxxDdmxxxList, Map<Integer, List<OrderDetail>> orderDetailMap) {
        for (Integer key : orderDetailMap.keySet()) {
            //不需要处理折扣行,因为处理被折扣行的时候,已作处理
            if (OrderDetail.InvoiceNature.DISCOUNT.getCode().equals(key))
                continue;
            List<OrderDetail> orderDetails = orderDetailMap.get(key);
            Iterator<OrderDetail> detailIterator = orderDetails.iterator();

            //处理被折扣行
            if (OrderDetail.InvoiceNature.DISCOUNTLINE.getCode().equals(key)) {
                while (detailIterator.hasNext()) {
                    OrderDetail orderDetail = detailIterator.next();
                    KpRequestyl.FpkjxxXmxx fpkjxxXmxx = createFpkjxxXmxx(enInfo, orderDetail);
                    if (fpkjxxXmxx == null)
                        return;
                    fpkjxxXmxxList.add(fpkjxxXmxx);//1发票明细
                    fpkjxxDdmxxxList.add(createFpkjxxDdmxxx(orderDetail));//1订单明细信息,不记录折扣行 一共两次
                    //处理完之后删除折扣行,因为可能存同名多个折扣的问题,所以这里根据同名,同数量,同编号解决
                    if (OrderDetail.InvoiceNature.DISCOUNTLINE.getCode().equals(orderDetail.getInvoiceNature())) {
                        Iterator<OrderDetail> discountIterator = orderDetailMap.get(OrderDetail.InvoiceNature.DISCOUNT.getCode()).iterator();
                        OrderDetail discountDetail = null;
                        while (discountIterator.hasNext()) {
                            OrderDetail discount = discountIterator.next();
//                            discount.getItemName().equals(orderDetail.getItemName()) && (discount.getItemNum() == null ? true : discount.getItemNum().equals(orderDetail.getItemNum())) && (discount.getTaxRate().equals(orderDetail.getTaxRate())) && discount.getItemTaxCode().equals(orderDetail.getItemTaxCode())
                            if (discount.getDiscountLine().equals(orderDetail.getRowNum())) {
                                discountDetail = CloneUtils.clone(discount);
                                discountIterator.remove();
                                break;
                            }
                        }
                        if (discountDetail == null) {
                            LocalError.getMapMessage().get().put(orderDetail.getOrderNo(), "单号:" + orderDetail.getOrderNo() + "商品:" + orderDetail.getItemName() + "缺少相关折扣行,开票失败!");
                            return;
                        }
                        KpRequestyl.FpkjxxXmxx fpkjxxXmxx2 = createFpkjxxXmxx(enInfo, discountDetail);
                        if (fpkjxxXmxx2 == null)
                            return;
                        fpkjxxXmxxList.add(fpkjxxXmxx2);//2发票明细
                    }
                }
            }

            //普通开票
            if (OrderDetail.InvoiceNature.NORMAL.getCode().equals(key)) {
                for (OrderDetail orderDetail : orderDetails) {
                    KpRequestyl.FpkjxxXmxx fpkjxxXmxx = createFpkjxxXmxx(enInfo, orderDetail);
                    if (fpkjxxXmxx == null)
                        return;
                    fpkjxxXmxxList.add(fpkjxxXmxx);//3发票明细
                    fpkjxxDdmxxxList.add(createFpkjxxDdmxxx(orderDetail));//2订单明细信息,无论是否折扣处理,都应该添加 订单明细信息 一共三次
                }
            }
        }
    }


    /**
     * 只处理红票 构建请求报文的项目明细,发票明细,订单明细信息,处理折扣行
     *
     * @param fpkjxxXmxxList
     * @param fpkjxxDdmxxxList
     */
    private void fillXmxxDdmxxxRed(EnInfo enInfo, List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList, List<KpRequestyl.FpkjxxDdmxxx> fpkjxxDdmxxxList, Map<Integer, List<OrderDetail>> orderDetailMap) {
        for (Integer key : orderDetailMap.keySet()) {
            //不需要处理折扣行,因为处理被折扣行的时候,已作处理
            if (OrderDetail.InvoiceNature.DISCOUNT.getCode().equals(key))
                continue;
            List<OrderDetail> orderDetails = orderDetailMap.get(key);
            Iterator<OrderDetail> detailIterator = orderDetails.iterator();

            //处理被折扣行
            if (OrderDetail.InvoiceNature.DISCOUNTLINE.getCode().equals(key)) {
                while (detailIterator.hasNext()) {
                    OrderDetail orderDetail = detailIterator.next();
                    fpkjxxDdmxxxList.add(createFpkjxxDdmxxx(orderDetail));//1订单明细信息,不记录折扣行 一共两次
                    //处理完之后删除折扣行,因为可能存同名多个折扣的问题,所以这里根据同名,同数量,同编号解决
                    if (OrderDetail.InvoiceNature.DISCOUNTLINE.getCode().equals(orderDetail.getInvoiceNature())) {
                        Iterator<OrderDetail> discountIterator = orderDetailMap.get(OrderDetail.InvoiceNature.DISCOUNT.getCode()).iterator();
                        OrderDetail discountDetail = null;
                        while (discountIterator.hasNext()) {
                            OrderDetail discount = discountIterator.next();
                            if (discount.getItemName().equals(orderDetail.getItemName()) && discount.getItemNum().equals(orderDetail.getItemNum()) && discount.getItemTaxCode().equals(orderDetail.getItemTaxCode())) {
                                discountDetail = CloneUtils.clone(discount);
                                discountIterator.remove();
                                break;
                            }
                        }
                        if (discountDetail == null) {
                            LocalError.getMapMessage().get().put(orderDetail.getOrderNo(), "单号:" + orderDetail.getOrderNo() + "商品:" + orderDetail.getItemName() + "缺少相关折扣行,冲红失败!");
                            return;
                        }


                        //合并正负折扣行
                        orderDetail.setItemPrice(TaxCalculationUtil.merge(orderDetail.getItemPrice(), discountDetail.getItemPrice()));
                        orderDetail.setItemNum(TaxCalculationUtil.negative(orderDetail.getItemNum()));
                        orderDetail.setInvoiceNature(OrderDetail.InvoiceNature.NORMAL.getCode());
                        KpRequestyl.FpkjxxXmxx fpkjxxXmxx = createFpkjxxXmxx(enInfo, orderDetail);
                        if (fpkjxxXmxx == null)
                            return;
                        fpkjxxXmxxList.add(fpkjxxXmxx);
                    }
                }
            }

            //普通开票
            if (OrderDetail.InvoiceNature.NORMAL.getCode().equals(key)) {
                for (OrderDetail orderDetail : orderDetails) {
                    orderDetail.setItemPrice(TaxCalculationUtil.negative(orderDetail.getItemPrice()));
                    orderDetail.setItemNum(TaxCalculationUtil.negative(orderDetail.getItemNum()));
                    KpRequestyl.FpkjxxXmxx fpkjxxXmxx = createFpkjxxXmxx(enInfo, orderDetail);
                    if (fpkjxxXmxx == null)
                        return;
                    fpkjxxXmxxList.add(fpkjxxXmxx);//3发票明细
                    fpkjxxDdmxxxList.add(createFpkjxxDdmxxx(orderDetail));//2订单明细信息,无论是否折扣处理,都应该添加 订单明细信息 一共三次
                }
            }
        }
    }

    /**
     * 构建发票明细
     *
     * @param orderDetail
     * @return
     */
    private KpRequestyl.FpkjxxXmxx createFpkjxxXmxx(EnInfo enInfo, OrderDetail orderDetail) {
        KpRequestyl.FpkjxxXmxx fpkjxxXmxx = new KpRequestyl.FpkjxxXmxx();
        fpkjxxXmxx.setXmmc(orderDetail.getItemName());    //项目名称
        fpkjxxXmxx.setXmdw(orderDetail.getItemUnit());   //项目单位
        fpkjxxXmxx.setGgxh(orderDetail.getSpecMode());   //规格型号
        fpkjxxXmxx.setXmsl(orderDetail.getItemNum());   //项目数量
        fpkjxxXmxx.setHsbz(orderDetail.getTaxIncluded());   //含税标志
        if (orderDetail.getItemPrice() != null) {
            fpkjxxXmxx.setXmdj(TaxCalculationUtil.scaleVIII(new BigDecimal(orderDetail.getItemPrice()).abs().toString()));   //项目单价 永远是正数
            fpkjxxXmxx.setXmje(TaxCalculationUtil.calcItemPrice(orderDetail.getItemPrice(), orderDetail.getItemNum()));   //项目金额
        } else if (orderDetail.getAmount() != null) {
            fpkjxxXmxx.setXmje(TaxCalculationUtil.scaleII(orderDetail.getAmount()));   //项目金额
        }

        fpkjxxXmxx.setFphxz(orderDetail.getInvoiceNature().toString());   //发票行性质
        fpkjxxXmxx.setSpbm(orderDetail.getItemTaxCode());   //商品编码
//        fpkjxxXmxx.setZxbm(orderDetail.getCuzCode());   //自行编码
        fpkjxxXmxx.setYhzcbs(enInfo.getYhzcbs());   //优惠政策标识
        fpkjxxXmxx.setLslbs(enInfo.getLslbs());   //零税率标识
        fpkjxxXmxx.setZzstsgl(enInfo.getZzstsgl());   //增值税特殊管理
        if (fpkjxxXmxx.getYhzcbs().equals("1") && fpkjxxXmxx.getZzstsgl() == null) {
            LocalError.getMapMessage().get().put(orderDetail.getOrderNo(), "单号:" + orderDetail.getOrderNo() + "使用优惠政策时,必须设置特殊增值税管理");
            return null;
        }


        fpkjxxXmxx.setSl(orderDetail.getTaxRate());   //税率

        if (OrderDetail.TaxIncludedFlag.INCLUDE.getCode().equals(orderDetail.getTaxIncluded())) {
            //计算含税标识为1的税额
            fpkjxxXmxx.setSe(TaxCalculationUtil.calcIncludeSE(fpkjxxXmxx.getSl(), fpkjxxXmxx.getXmje()));   //税额
        } else if (OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode().equals(orderDetail.getTaxIncluded())) {
            //计算含税标识为0的税额
            fpkjxxXmxx.setSe(TaxCalculationUtil.calcNotIncludeSE(fpkjxxXmxx.getSl(), fpkjxxXmxx.getXmje()));   //税额
        }

//            fpkjxxXmxx.setByzd1();   //备用字段1
//            fpkjxxXmxx.setByzd2();   //备用字段2
//            fpkjxxXmxx.setByzd3();   //备用字段3
//            fpkjxxXmxx.setByzd4();   //备用字段4
//            fpkjxxXmxx.setByzd5();   //备用字段5
        return fpkjxxXmxx;
    }

    /**
     * 构建订单明细
     *
     * @param orderDetail
     * @return
     */
    private KpRequestyl.FpkjxxDdmxxx createFpkjxxDdmxxx(OrderDetail orderDetail) {
        KpRequestyl.FpkjxxDdmxxx fpkjxxDdmxxx = new KpRequestyl.FpkjxxDdmxxx();
        fpkjxxDdmxxx.setDdmc(orderDetail.getOrderNo());    //订单名称
        fpkjxxDdmxxx.setDw(orderDetail.getItemUnit()); //单位
        fpkjxxDdmxxx.setGgxh(orderDetail.getSpecMode()); //规格型号
        fpkjxxDdmxxx.setSl(orderDetail.getItemNum()); //数量
        if (orderDetail.getItemPrice() != null)
            fpkjxxDdmxxx.setDj(TaxCalculationUtil.scaleVIII(orderDetail.getItemPrice())); //单价
        if (orderDetail.getAmount() != null)
            fpkjxxDdmxxx.setJe(TaxCalculationUtil.scaleII(orderDetail.getAmount())); //金额
//        fpkjxxDdmxxx.setByzd1(); //备用字段1
//        fpkjxxDdmxxx.setByzd2(); //备用字段2
//        fpkjxxDdmxxx.setByzd3(); //备用字段3
//        fpkjxxDdmxxx.setByzd4(); //备用字段4
//        fpkjxxDdmxxx.setByzd5(); //备用字段5
        return fpkjxxDdmxxx;
    }


    /**
     * 下载pdf帮助类
     */
    public static class DownLoadHelper {
        //XML 请求报文是否成功标识
        private boolean success;
        //XML 请求错误信息
        private String errorMsg;

        private InvoiceInfo invoiceInfo;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public InvoiceInfo getInvoiceInfo() {
            return invoiceInfo;
        }

        public void setInvoiceInfo(InvoiceInfo invoiceInfo) {
            this.invoiceInfo = invoiceInfo;
        }


        public void updateDownloadCount(InvoiceInfoService invoiceInfoService, Integer shardingId) {
            //下载成功后,下载次数+1
            invoiceInfo.setDownloadCount(invoiceInfo.getDownloadCount() + 1);
            //累计下载次数+1
            invoiceInfo.setAccDownloadCount(invoiceInfo.getAccDownloadCount() + 1);
            invoiceInfo.setInsertDate(DateUtil.newDate());
            invoiceInfoService.updateEntity(invoiceInfo, shardingId);
        }
    }


//------------------------------枚举------------------------


    /**
     *
     */
    public enum BillingOptionType {

        AUTO("0", "自动"), MANUAL("1", "手动");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        BillingOptionType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (BillingOptionType item : BillingOptionType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] billingOptionTypeParams = new Parameter[BillingOptionType.values().length];

            for (int i = 0; i < billingOptionTypeParams.length; i++) {
                billingOptionTypeParams[i] = new Parameter(BillingOptionType.values()[i].getCode().toString(), BillingOptionType.values()[i].getName());
            }
            return billingOptionTypeParams;
        }


        //-----------------------------------getter and setter---------------------------------------------------------


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
