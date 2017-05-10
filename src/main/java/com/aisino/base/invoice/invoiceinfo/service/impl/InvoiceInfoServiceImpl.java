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
import com.aisino.common.model.xml.impl.KpRequestyl;
import com.aisino.common.model.xml.impl.Requestt;
import com.aisino.common.model.xml.impl.ResponseFpkj;
import com.aisino.common.model.xml.impl.XzRequest;
import com.aisino.common.model.xml.util.XmlModelUtil;
import com.aisino.common.util.AesUtil;
import com.aisino.core.mybatis.DataSourceContextHolder;
import com.aisino.core.service.BaseServiceImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private GlobalInfoParams globalInfoParams;

    @Resource
    private DataDescriptionParams dataDescriptionParams;

    @Resource
    private RequestParams requestParams;


    @Override
    public void downloadRequest(Integer invoiceId,String usrNo) {
        DataSourceContextHolder.user();
        InvoiceInfo invoiceInfo = this.findEntityById(invoiceId);
        DataSourceContextHolder.write();
        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(usrNo);
        XzRequest xzRequest = new XzRequest(invoiceInfo.getSerialNo(), authCodeInfo.getPlatformCode(), authCodeInfo.getTaxNo(), invoiceInfo.getOrderNo());
        String xmlContent = XmlModelUtil.beanToXmlStr(xzRequest,XzRequest.class);
        Requestt requestt = careateRequestt(usrNo);
        requestt.getData().setContent(xmlContent);
        String info = XmlModelUtil.doPost(requestt,Requestt.class,requestParams.getUrl());

        System.out.println();
    }


    /**
     * @return
     */
    @Override
    public Integer requestBilling(String usrNo, Integer orderInfoId, InvoiceInfo.InvoiceType invoiceType) {
        Requestt requestt = careateRequestt(usrNo);
        KpRequestyl.RequestFpkjxx requestFpkjxx = createKpRequestyl(usrNo, orderInfoId, invoiceType);
        String xmlContent = XmlModelUtil.beanToXmlStr(requestFpkjxx, KpRequestyl.RequestFpkjxx.class);
        try {
            requestt.getData().setContent(AesUtil.encrypt(xmlContent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return billing(usrNo,requestt, invoiceType, requestFpkjxx);
    }


    private Requestt careateRequestt(String usrNo) {
        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(usrNo);

        Requestt requestt = new Requestt();
        Requestt.GlobalInfo globalInfo = new Requestt.GlobalInfo();

        globalInfo.setAppId(globalInfoParams.getAppId());
        globalInfo.setResponseCode(globalInfoParams.getResponseCode());
        globalInfo.setTerminalCode(globalInfoParams.getTerminalCode());
        globalInfo.setVersion(globalInfoParams.getVersion());
        globalInfo.setInterfaceCode("DFXJ1009");
        globalInfo.setUserName(authCodeInfo.getPlatformCode());
        globalInfo.setRequestCode("P0000001");
        globalInfo.setPassWord("5600163257ixlV0FoSGORAjfsesyE+oQ==");
//        globalInfo.setPassWord(PasswordUtil.getPassword(authCodeInfo.getRegiCode()));
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
    private Integer billing(String usrNo,Requestt requestt, InvoiceInfo.InvoiceType invoiceType,KpRequestyl.RequestFpkjxx requestFpkjxx) {

        String info = XmlModelUtil.doPost(requestt,Requestt.class,requestParams.getUrl());

        Requestt result = XmlModelUtil.xmlStrToBean(info,Requestt.class);
        //解析返回信息
        String returnCode = result.getReturnStateInfo().getReturnCode();

        //如果成功返回信息,保存发票信息
        if (returnCode.equals("0000")) {
            String decrptContent = null;
            try {
                decrptContent = AesUtil.decrypt(result.getData().getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            DataSourceContextHolder.user();
            Integer id = addByResponseFpkj(decrptContent, invoiceType,usrNo, requestFpkjxx);
            DataSourceContextHolder.write();
            return id;
        }
        throw new RuntimeException(result.getReturnStateInfo().getReturnMessage());
    }

    public Integer addByResponseFpkj(String decrptContent, InvoiceInfo.InvoiceType invoiceType, String usrno, KpRequestyl.RequestFpkjxx requestFpkjxx){
        ResponseFpkj responseFpkj = XmlModelUtil.xmlStrToBean(decrptContent,ResponseFpkj.class);
        InvoiceInfo invoiceInfo = new InvoiceInfo();
//            invoiceInfo.setSerialNo();
        invoiceInfo.setProjectName(requestFpkjxx.getFpkjxxFptxx().getKpxm());
        invoiceInfo.setOrderNo(requestFpkjxx.getFpkjxxDdxx().getDdh());
        invoiceInfo.setNoTaxTotal(responseFpkj.getHjbhsje());
        invoiceInfo.setTaxTotal(responseFpkj.getHjse());
        invoiceInfo.setInvoiceDate(new Date(Long.parseLong(responseFpkj.getKprq())));
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
    private KpRequestyl.RequestFpkjxx createKpRequestyl(String usrNo, Integer orderInfoId, InvoiceInfo.InvoiceType invoiceType) {
        DataSourceContextHolder.user();
        OrderInfo orderInfo = orderInfoService.findEntityById(orderInfoId);
        DataSourceContextHolder.write();
        if (orderInfo == null)
            return null;

        EnInfo enInfo = enInfoService.getByUsrno(usrNo);

        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(usrNo);

        KpRequestyl.RequestFpkjxx requestFpkjxx = new KpRequestyl.RequestFpkjxx();

        KpRequestyl.FpkjxxFptxx fpkjxxFptxx = new KpRequestyl.FpkjxxFptxx();
        fpkjxxFptxx.setFpqqlsh("3453417744");//发票请求唯一流水号
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
//        fpkjxxFptxx.setYfpdm();  //原发票代码
//        fpkjxxFptxx.setYfphm();  //原发票号码
        /**
         *   10 正票正常开具
         *   11 正票错票重开
         *   20 退货折让红票、
         *   21 错票重开红票、
         *   22 换票冲红（全冲
         *   红电子发票，开具
         *   纸质发票）
         */
//        fpkjxxFptxx.setCzdm();  //操作代码
        /**
         * 0：根据项目名称字数，自动产生清单， 保持目前逻辑不变     1：取清单对应票面内容字段打印到发信息 XMXX 打印到清  单上。默认为 0。
         */
//        fpkjxxFptxx.setQdbz();  //清单标志
        /**
         * 需要打印清单时对 应发票票面项目名 称 清单标识（ QD_BZ） 为 1 时必填。 为 0 不进行   处理
         */
//        fpkjxxFptxx.setQdxmmc();  //清单发票项目名称
//        fpkjxxFptxx.setChyy();  //冲红原因 冲红时填写，由企 业定义
//        fpkjxxFptxx.setTschbz();  //特殊冲红标志
////        fpkjxxFptxx.setKphjje(orderInfo.get);  //价税合计金额 小数点后 元为单位精确到分 2 位，
//        fpkjxxFptxx.setHjbhsje();  //合计不含税金额 小数点后 2 位，以     元为单位精确到分（单行商品金额之   和）。 平台处理价税        分离，此值传 0
//        fpkjxxFptxx.setHjse();  //合计税额 小数点后 2 位，以     元为单位精确到分         (单行商品税额之    和)，平台处理价税      分离，此值传 0
//        fpkjxxFptxx.setBz();  //备注
//        fpkjxxFptxx.setByzd1();  //备用字段1
//        fpkjxxFptxx.setByzd2();  //备用字段2
//        fpkjxxFptxx.setByzd3();  //备用字段3
//        fpkjxxFptxx.setByzd4();  //备用字段4
//        fpkjxxFptxx.setByzd5();  //备用字段5

        requestFpkjxx.setFpkjxxFptxx(fpkjxxFptxx);

        KpRequestyl.FpkjxxXmxxs fpkjxxXmxxs = new KpRequestyl.FpkjxxXmxxs();

        DataSourceContextHolder.user();
        List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(orderInfo.getOrderNo());
        DataSourceContextHolder.write();
        List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList = new ArrayList<>(orderDetails.size());

        for (OrderDetail orderDetail : orderDetails) {
            KpRequestyl.FpkjxxXmxx fpkjxxXmxx = new KpRequestyl.FpkjxxXmxx();
            fpkjxxXmxx.setXmmc(orderDetail.getItemName());    //项目名称
            fpkjxxXmxx.setXmdw(orderDetail.getItemUnit());   //项目单位
            fpkjxxXmxx.setGgxh(orderDetail.getSpecMode());   //规格型号
            fpkjxxXmxx.setXmsl(orderDetail.getItemNum());   //项目数量
//            fpkjxxXmxx.setHsbz(orderDetail.get);   //含税标志
            fpkjxxXmxx.setXmdj(orderDetail.getItemPrice());   //项目单价
            fpkjxxXmxx.setFphxz(orderDetail.getInvoiceNature().toString());   //发票行性质
//            fpkjxxXmxx.setSpbm(orderDetail.get);   //商品编码
//            fpkjxxXmxx.setZxbm();   //自行编码
//            fpkjxxXmxx.setYhzcbs();   //优惠政策标识
//            fpkjxxXmxx.setLslbs();   //零税率标识
//            fpkjxxXmxx.setZzstsgl();   //增值税特殊管理
//            fpkjxxXmxx.setXmje();   //项目金额
            fpkjxxXmxx.setSl(orderDetail.getTaxRate());   //税率
//            fpkjxxXmxx.setSe();   //税额
//            fpkjxxXmxx.setByzd1();   //备用字段1
//            fpkjxxXmxx.setByzd2();   //备用字段2
//            fpkjxxXmxx.setByzd3();   //备用字段3
//            fpkjxxXmxx.setByzd4();   //备用字段4
//            fpkjxxXmxx.setByzd5();   //备用字段5

            fpkjxxXmxxList.add(fpkjxxXmxx);
        }


        fpkjxxXmxxs.setSize(fpkjxxXmxxList.size() + "");
        fpkjxxXmxxs.setFpkjxxXmxxList(fpkjxxXmxxList);


        requestFpkjxx.setFpkjxxXmxxs(fpkjxxXmxxs);

        //项目信息
        KpRequestyl.FpkjxxDdxx fpkjxxDdxx = new KpRequestyl.FpkjxxDdxx();
        fpkjxxDdxx.setDdh(orderInfo.getOrderNo());   //订单号
//        fpkjxxDdxx.setThdh();  //退货单号
        fpkjxxDdxx.setDddate(DateFormatUtils.format(orderInfo.getOrderDate(), "yyyy-MM-dd HH:mm:ss SS"));  //订单日期

        requestFpkjxx.setFpkjxxDdxx(fpkjxxDdxx);

        //订单明细
        KpRequestyl.FpkjxxDdmxxxs fpkjxxDdmxxxs = new KpRequestyl.FpkjxxDdmxxxs();

        List<KpRequestyl.FpkjxxDdmxxx> fpkjxxDdmxxxList = new ArrayList<>();

        //(XMXX)项目信息（ 发票明细）（ 多条）
        KpRequestyl.FpkjxxDdmxxx fpkjxxDdmxxxBuilder = new KpRequestyl.FpkjxxDdmxxx();
//        fpkjxxDdmxxxBuilder.setDdmc();    //订单名称
//        fpkjxxDdmxxxBuilder.setDw(); //单位
//        fpkjxxDdmxxxBuilder.setGgxh(); //规格型号
//        fpkjxxDdmxxxBuilder.setSl(); //数量
//        fpkjxxDdmxxxBuilder.setDj(); //单价
//        fpkjxxDdmxxxBuilder.setJe(); //金额
//        fpkjxxDdmxxxBuilder.setByzd1(); //备用字段1
//        fpkjxxDdmxxxBuilder.setByzd2(); //备用字段2
//        fpkjxxDdmxxxBuilder.setByzd3(); //备用字段3
//        fpkjxxDdmxxxBuilder.setByzd4(); //备用字段4
//        fpkjxxDdmxxxBuilder.setByzd5(); //备用字段5

        fpkjxxDdmxxxList.add(fpkjxxDdmxxxBuilder);


        fpkjxxDdmxxxs.setSize(fpkjxxDdmxxxList.size() + "");
        fpkjxxDdmxxxs.setFpkjxxDdmxxxList(fpkjxxDdmxxxList);

        requestFpkjxx.setFpkjxxDdmxxxs(fpkjxxDdmxxxs);

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

        return requestFpkjxx;
    }


}
