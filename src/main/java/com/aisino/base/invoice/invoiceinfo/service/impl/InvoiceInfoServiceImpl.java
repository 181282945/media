package com.aisino.base.invoice.invoiceinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.authcodeinfo.params.DataDescriptionParams;
import com.aisino.base.invoice.authcodeinfo.params.RequestParams;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.invoiceinfo.dao.InvoiceInfoMapper;
import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.base.invoice.invoiceinfo.service.InvoiceInfoService;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.common.model.xml.impl.*;
import com.aisino.common.model.xml.util.XmlModelUtil;
import com.aisino.common.util.AesUtil;
import com.aisino.common.util.Base64;
import com.aisino.common.util.IdWorker;
import com.aisino.common.util.PasswordUtil;
import com.aisino.common.util.tax.TaxCalculationUtil;
import com.aisino.core.mybatis.DataSourceContextHolder;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.CloneUtils;
import com.aisino.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 为 on 2017-4-28.
 */
@Service("invoiceInfoService")
public class InvoiceInfoServiceImpl extends BaseServiceImpl<InvoiceInfo, InvoiceInfoMapper> implements InvoiceInfoService {

    @Resource
    private AuthCodeInfoService authCodeInfoService;

    @Resource
    private EnInfoService enInfoService;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private InvoiceInfoService invoiceInfoService;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    @Resource
    private IdWorker worker;

    @Resource
    private GlobalInfoParams globalInfoParams;

    @Resource
    private DataDescriptionParams dataDescriptionParams;

    @Resource
    private RequestParams requestParams;


    @Override
    public InvoiceInfo getBySerialNo(String serialNo) {
        Specification<InvoiceInfo> specification = new Specification<>(InvoiceInfo.class);
        specification.addQueryLike(new QueryLike("serialNo", QueryLike.LikeMode.Eq, serialNo));
        return this.getOne(specification);
    }

    @Override
    public String downloadRequest(Integer invoiceId, String usrNo) {
        DataSourceContextHolder.user();
        InvoiceInfo invoiceInfo = this.findEntityById(invoiceId);
        DataSourceContextHolder.write();
        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(usrNo);
        XzRequest xzRequest = new XzRequest(invoiceInfo.getSerialNo(), authCodeInfo.getPlatformCode(), authCodeInfo.getTaxNo(), invoiceInfo.getOrderNo());
        String xmlContent = XmlModelUtil.beanToXmlStr(xzRequest, XzRequest.class);
        Requestt requestt = careateRequestt(usrNo, globalInfoParams.getDownloadInterfaceCode());
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));
        String info = XmlModelUtil.doPost(requestt, Requestt.class, requestParams.getUrl());
        Requestt result = XmlModelUtil.xmlStrToBean(info, Requestt.class);

        if (Requestt.SUCCESS_CODE.equals(result.getReturnStateInfo().getReturnCode())) {
            ResponseFpxxxz responseFpxxxz = XmlModelUtil.xmlStrToBean(AesUtil.decrypt(result.getData().getContent()), ResponseFpxxxz.class);
            return responseFpxxxz.getPdfUrl();
        }

        throw new RuntimeException(Base64.getFromBase64(result.getReturnStateInfo().getReturnMessage()));
    }


    /**
     * @return
     */
    @Override
    public Integer requestBilling(String usrNo, Integer orderInfoId, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType, String reMarks) {
        KpRequestyl.RequestFpkjxx requestFpkjxx = createKpRequestyl(usrNo, orderInfoId, invoiceType, czdmType, reMarks);
        Requestt requestt = careateRequestt(usrNo, globalInfoParams.getBillingInterfaceCode());
        String xmlContent = XmlModelUtil.beanToXmlStr(requestFpkjxx, KpRequestyl.RequestFpkjxx.class);
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));
        return billing(usrNo, requestt, invoiceType, requestFpkjxx);
    }


    private Requestt careateRequestt(String usrNo, String interfaceCode) {
        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(usrNo);

        Requestt requestt = new Requestt();
        Requestt.GlobalInfo globalInfo = new Requestt.GlobalInfo();

        globalInfo.setAppId(globalInfoParams.getAppId());
        globalInfo.setResponseCode(globalInfoParams.getResponseCode());
        globalInfo.setTerminalCode(globalInfoParams.getTerminalCode());
        globalInfo.setVersion(globalInfoParams.getVersion());
        globalInfo.setInterfaceCode(interfaceCode);
        globalInfo.setUserName(authCodeInfo.getPlatformCode());
        globalInfo.setRequestCode(globalInfoParams.getRequestCode());
        globalInfo.setPassWord(PasswordUtil.getPassword(authCodeInfo.getRegiCode()));
        globalInfo.setRequestTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SS"));
        globalInfo.setDataExchangeId(globalInfoParams.getDataExchangeId());
        globalInfo.setTaxpayerId(authCodeInfo.getTaxNo());
        globalInfo.setAuthorizationCode(authCodeInfo.getAuthCode());
        requestt.setGlobalInfo(globalInfo);

        Requestt.ReturnStateInfo returnStateInfo = new Requestt.ReturnStateInfo();
        requestt.setReturnStateInfo(returnStateInfo);
        Requestt.Data data = new Requestt.Data();


        Requestt.DataDescription dataDescription = new Requestt.DataDescription();
        dataDescription.setCodeType(dataDescriptionParams.getCodeType());
        dataDescription.setEncryptCode(dataDescriptionParams.getEncryptCode());
        dataDescription.setZipCode(dataDescriptionParams.getZipCode());

        data.setDataDescription(dataDescription);

        requestt.setData(data);
        return requestt;
    }


    /**
     * 开票
     */
    private Integer billing(String usrNo, Requestt requestt, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.RequestFpkjxx requestFpkjxx) {

        String info = XmlModelUtil.doPost(requestt, Requestt.class, requestParams.getUrl());

        Requestt result = XmlModelUtil.xmlStrToBean(info, Requestt.class);
        //解析返回信息
        String returnCode = result.getReturnStateInfo().getReturnCode();

        //如果成功返回信息,保存发票信息
        if (Requestt.SUCCESS_CODE.equals(returnCode)) {
            String decrptContent = AesUtil.decrypt(result.getData().getContent());
            DataSourceContextHolder.user();
            Integer id = addByResponseFpkj(decrptContent, invoiceType, usrNo, requestFpkjxx);
            DataSourceContextHolder.write();
            return id;
        }
        throw new RuntimeException(Base64.getFromBase64(result.getReturnStateInfo().getReturnMessage()));
    }

    public Integer addByResponseFpkj(String decrptContent, InvoiceInfo.InvoiceType invoiceType, String usrno, KpRequestyl.RequestFpkjxx requestFpkjxx) {
        ResponseFpkj responseFpkj = XmlModelUtil.xmlStrToBean(decrptContent, ResponseFpkj.class);
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setSerialNo(requestFpkjxx.getFpkjxxFptxx().getFpqqlsh());
        invoiceInfo.setProjectName(requestFpkjxx.getFpkjxxFptxx().getKpxm());
        invoiceInfo.setOrderNo(requestFpkjxx.getFpkjxxDdxx().getDdh());
        invoiceInfo.setNoTaxTotal(responseFpkj.getHjbhsje());
        invoiceInfo.setPriceTotal(requestFpkjxx.getFpkjxxFptxx().getKphjje());
        invoiceInfo.setTaxTotal(responseFpkj.getHjse());
        invoiceInfo.setInvoiceDate(DateUtil.parseDate(DateUtil.KPRQ_PATTON, responseFpkj.getKprq()));
        invoiceInfo.setPdfUrl(responseFpkj.getPdfurl());
        invoiceInfo.setInvoiceType(invoiceType.getCode());
        invoiceInfo.setUsrno(usrno);
        invoiceInfo.setInvoiceCode(responseFpkj.getFpdm());
        invoiceInfo.setInvoiceNum(responseFpkj.getFphm());
        return this.addEntity(invoiceInfo);
    }


    /**
     * @return
     */
    private KpRequestyl.RequestFpkjxx createKpRequestyl(final String usrNo, final Integer orderInfoId, final InvoiceInfo.InvoiceType invoiceType, final KpRequestyl.FpkjxxFptxx.CzdmType czdmType, String reMarks) {
        //为了节省连接数,尽可能在一次切换里获取需要的数据
        DataSourceContextHolder.user();
        final OrderInfo[] orderInfo = new OrderInfo[1];
        final InvoiceInfo[] orginalInvoiceInfo = new InvoiceInfo[1];
        final List<OrderDetail> orderDetails = new ArrayList<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                orderInfo[0] = orderInfoService.findEntityById(orderInfoId);
                if (orderInfo[0] == null)
                    throw new RuntimeException("没有找到该订单!");
                if (OrderInfo.StatusType.ALREADY.getCode().equals(orderInfo[0].getStatus()) && KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL.equals(czdmType) && InvoiceInfo.InvoiceType.NORMAL.equals(invoiceType))
                    throw new RuntimeException("此订单已经开票,不能重复开票!");
                if (StringUtils.isBlank(orderInfo[0].getSerialNo()))
                {
                    orderInfo[0].setSerialNo(String.valueOf(worker.nextId()));//设置发票唯一流水号
                    orderInfoService.updateEntity(orderInfo[0]);
                }
                if (InvoiceInfo.InvoiceType.RED.equals(invoiceType)) {
                    orginalInvoiceInfo[0] = invoiceInfoService.getBySerialNo(orderInfo[0].getSerialNo());//正式环境用这个
                    if (orginalInvoiceInfo[0] == null)
                        throw new RuntimeException("没有找到原发票信息,冲红失败!");
                    if (InvoiceInfo.RedflagsType.ALREADY.getCode().equals(orginalInvoiceInfo[0].getRedflags()))
                        throw new RuntimeException("此发票不能重复冲红!");
                    if(orginalInvoiceInfo[0].getSerialNoRed() == null){
                        orginalInvoiceInfo[0].setSerialNoRed(String.valueOf(worker.nextId()));//设置红票唯一流水号
                        invoiceInfoService.updateEntity(orginalInvoiceInfo[0]);
                    }
                }
                orderDetails.addAll(orderDetailService.findByOrderNo(orderInfo[0].getOrderNo()));
            }
        });
        DataSourceContextHolder.write();

        final EnInfo[] enInfo = new EnInfo[1];
        final AuthCodeInfo[] authCodeInfo = new AuthCodeInfo[1];

        transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setReadOnly(true);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                enInfo[0] = enInfoService.getByUsrno(usrNo);

                authCodeInfo[0] = authCodeInfoService.getByUsrno(usrNo);
            }
        });


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
            throw new RuntimeException("折扣行与被折扣行数量不匹配,开票失败!");
        }


        List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList = new ArrayList<>(orderDetails.size());
        List<KpRequestyl.FpkjxxDdmxxx> fpkjxxDdmxxxList = new ArrayList<>();


//        List<OrderDetail> discounts = orderDetailMap.get(OrderDetail.InvoiceNature.DISCOUNT.getCode());


        //开正票
        if (InvoiceInfo.InvoiceType.NORMAL.equals(invoiceType))
            //先构建 折扣行与被折扣行
            fillXmxxDdmxxx(enInfo[0], fpkjxxXmxxList, fpkjxxDdmxxxList, orderDetailMap);
        //开红票
        if (InvoiceInfo.InvoiceType.RED.equals(invoiceType))
            //先构建 折扣行与被折扣行
            fillXmxxDdmxxxRed(enInfo[0], fpkjxxXmxxList, fpkjxxDdmxxxList, orderDetailMap);


        fpkjxxXmxxs.setSize(fpkjxxXmxxList.size() + "");
        fpkjxxXmxxs.setFpkjxxXmxxList(fpkjxxXmxxList);

        requestFpkjxx.setFpkjxxXmxxs(fpkjxxXmxxs);

        fpkjxxDdmxxxs.setSize(fpkjxxDdmxxxList.size() + "");
        fpkjxxDdmxxxs.setFpkjxxDdmxxxList(fpkjxxDdmxxxList);

        requestFpkjxx.setFpkjxxDdmxxxs(fpkjxxDdmxxxs);


        //项目信息
        KpRequestyl.FpkjxxDdxx fpkjxxDdxx = new KpRequestyl.FpkjxxDdxx();
        fpkjxxDdxx.setDdh(orderInfo[0].getOrderNo());   //订单号
//        fpkjxxDdxx.setThdh();  //退货单号
        fpkjxxDdxx.setDddate(DateFormatUtils.format(orderInfo[0].getOrderDate(), "yyyy-MM-dd HH:mm:ss SS"));  //订单日期

        requestFpkjxx.setFpkjxxDdxx(fpkjxxDdxx);


        //支付信息
        KpRequestyl.FpkjxxZfxx fpkjxxZfxx = new KpRequestyl.FpkjxxZfxx();
//        fpkjxxZfxx.setZffs();    //支付方式
//        fpkjxxZfxx.setZflsh();   //支付流水号
//        fpkjxxZfxx.setZfpt();    //支付平台

        requestFpkjxx.setFpkjxxZfxx(fpkjxxZfxx);

        //物流信息
        KpRequestyl.FpkjxxWlxx fpkjxxWlxx = new KpRequestyl.FpkjxxWlxx();
//        fpkjxxWlxx.setCygs();   //承运公司
//        fpkjxxWlxx.setShsj();   //送货时间
//        fpkjxxWlxx.setWldh();   //物流单号
//        fpkjxxWlxx.setShdz();   //送货地址


        requestFpkjxx.setFpkjxxWlxx(fpkjxxWlxx);

        KpRequestyl.FpkjxxFptxx fpkjxxFptxx = createFpkjxxFptxx(authCodeInfo[0], enInfo[0], orderInfo[0], orginalInvoiceInfo[0], fpkjxxXmxxList, invoiceType, czdmType, reMarks);
        requestFpkjxx.setFpkjxxFptxx(fpkjxxFptxx);

        return requestFpkjxx;
    }

    /**
     * 创建请求报文
     *
     * @param authCodeInfo
     * @param enInfo
     * @param orderInfo
     * @param orginalInvoiceInfo
     * @param fpkjxxXmxxList
     * @param invoiceType
     * @param czdmType
     * @return
     */
    private KpRequestyl.FpkjxxFptxx createFpkjxxFptxx(AuthCodeInfo authCodeInfo, EnInfo enInfo, OrderInfo orderInfo, InvoiceInfo orginalInvoiceInfo, List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList, InvoiceInfo.InvoiceType invoiceType, KpRequestyl.FpkjxxFptxx.CzdmType czdmType, String reMarks) {
        KpRequestyl.FpkjxxFptxx fpkjxxFptxx = new KpRequestyl.FpkjxxFptxx();
        fpkjxxFptxx.setFpqqlsh(orderInfo.getSerialNo());//发票请求唯一流水号
        fpkjxxFptxx.setDsptbm(authCodeInfo.getPlatformCode());//平台编码
        fpkjxxFptxx.setNsrsbh(enInfo.getTaxno());//开票方识别号
        fpkjxxFptxx.setNsrmc(enInfo.getTaxname());//开票方名称
        fpkjxxFptxx.setNsrdzdah(authCodeInfo.getElecNmber()); //开票方电子档案号
        fpkjxxFptxx.setSwjgdm(authCodeInfo.getTaxCode()); //税务机构代码
        fpkjxxFptxx.setDkbz(orderInfo.getDkflags().toString()); //代开标志
        fpkjxxFptxx.setPydm(orderInfo.getTicketCode()); //票样代码
        fpkjxxFptxx.setKpxm(orderInfo.getMajorItems());   //主要开票项目
        fpkjxxFptxx.setBmbbbh(authCodeInfo.getCodeTableVer());  //编码表版本号
        fpkjxxFptxx.setXhfnsrsbh(enInfo.getTaxno()); //销货方识别号
        fpkjxxFptxx.setXhfmc(enInfo.getTaxname());   //销货方名称
        fpkjxxFptxx.setXhfdz(enInfo.getTaxaddr());  //销货方地址
        fpkjxxFptxx.setXhfdh(enInfo.getTaxtel());  //销货方电话
        fpkjxxFptxx.setXhfyhzh(enInfo.getBankaccount());  //销货方银行账号
        fpkjxxFptxx.setGhfmc(orderInfo.getBuyerName());  //购货方名称
        fpkjxxFptxx.setGhfnsrsbh(orderInfo.getBuyerTaxno());    //购货方识别号
        fpkjxxFptxx.setGhfsf(orderInfo.getBuyerProvince());  //购货方省份
        fpkjxxFptxx.setGhfdz(orderInfo.getBuyerAddr());  //购货方地址
        fpkjxxFptxx.setGhfgddh(orderInfo.getBuyerTele());  //购货方固定电话
        fpkjxxFptxx.setGhfsj(orderInfo.getBuyerMobile());  //购货方手机
        fpkjxxFptxx.setGhfemail(orderInfo.getBuyerEmail());  //购货方邮箱
        fpkjxxFptxx.setGhfqylx(orderInfo.getBuyerType());  //购货方企业类型
        fpkjxxFptxx.setGhfyhzh(orderInfo.getBuyerBankAcc());  //购货方银行账号
        fpkjxxFptxx.setHydm(orderInfo.getInduCode());  //行业代码
        fpkjxxFptxx.setHymc(orderInfo.getInduName());    //行业名称
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
                    fpkjxxXmxxList.add(createFpkjxxXmxx(enInfo, orderDetail));//1发票明细
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
                        if (discountDetail == null)
                            throw new RuntimeException("商品:" + orderDetail.getItemName() + "缺少相关折扣行,开票失败,");
                        fpkjxxXmxxList.add(createFpkjxxXmxx(enInfo, discountDetail));//2发票明细
                    }
                }
            }

            //普通开票
            if (OrderDetail.InvoiceNature.NORMAL.getCode().equals(key)) {
                for (OrderDetail orderDetail : orderDetails) {
                    fpkjxxXmxxList.add(createFpkjxxXmxx(enInfo, orderDetail));//3发票明细
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
                        if (discountDetail == null)
                            throw new RuntimeException("商品:" + orderDetail.getItemName() + "缺少相关折扣行,冲红失败.");

                        //合并正负折扣行
                        orderDetail.setItemPrice(TaxCalculationUtil.merge(orderDetail.getItemPrice(), discountDetail.getItemPrice()));
                        orderDetail.setItemNum(TaxCalculationUtil.negative(orderDetail.getItemNum()));
                        orderDetail.setInvoiceNature(OrderDetail.InvoiceNature.NORMAL.getCode());
                        fpkjxxXmxxList.add(createFpkjxxXmxx(enInfo, orderDetail));
                    }
                }
            }

            //普通开票
            if (OrderDetail.InvoiceNature.NORMAL.getCode().equals(key)) {
                for (OrderDetail orderDetail : orderDetails) {
                    orderDetail.setItemPrice(TaxCalculationUtil.negative(orderDetail.getItemPrice()));
                    orderDetail.setItemNum(TaxCalculationUtil.negative(orderDetail.getItemNum()));
                    fpkjxxXmxxList.add(createFpkjxxXmxx(enInfo, orderDetail));//3发票明细
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
        fpkjxxXmxx.setXmdj(TaxCalculationUtil.scaleVIII(new BigDecimal(orderDetail.getItemPrice()).abs().toString()));   //项目单价 永远是正数
        fpkjxxXmxx.setFphxz(orderDetail.getInvoiceNature().toString());   //发票行性质
        fpkjxxXmxx.setSpbm(orderDetail.getItemTaxCode());   //商品编码
        fpkjxxXmxx.setZxbm(orderDetail.getCuzCode());   //自行编码
        fpkjxxXmxx.setYhzcbs(enInfo.getYhzcbs());   //优惠政策标识
        fpkjxxXmxx.setLslbs(enInfo.getLslbs());   //零税率标识
        fpkjxxXmxx.setZzstsgl(enInfo.getZzstsgl());   //增值税特殊管理
        if (fpkjxxXmxx.getYhzcbs().equals("1") && fpkjxxXmxx.getZzstsgl() == null)
            throw new RuntimeException("使用优惠政策时,必须设置特殊增值税管理");

        fpkjxxXmxx.setXmje(TaxCalculationUtil.calcItemPrice(orderDetail.getItemPrice(), orderDetail.getItemNum()));   //项目金额
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
        fpkjxxDdmxxx.setDj(TaxCalculationUtil.scaleVIII(orderDetail.getItemPrice())); //单价
        fpkjxxDdmxxx.setJe(TaxCalculationUtil.scaleII(orderDetail.getItemPrice())); //金额
//        fpkjxxDdmxxx.setByzd1(); //备用字段1
//        fpkjxxDdmxxx.setByzd2(); //备用字段2
//        fpkjxxDdmxxx.setByzd3(); //备用字段3
//        fpkjxxDdmxxx.setByzd4(); //备用字段4
//        fpkjxxDdmxxx.setByzd5(); //备用字段5
        return fpkjxxDdmxxx;
    }

}
