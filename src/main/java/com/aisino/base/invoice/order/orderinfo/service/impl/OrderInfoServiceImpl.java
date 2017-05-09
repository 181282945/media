package com.aisino.base.invoice.order.orderinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.authcodeinfo.params.RequesttParams;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.common.model.KpInterface;
import com.aisino.common.model.xml.KpRequestyl;
import com.aisino.common.model.xml.Requestt;
import com.aisino.common.util.*;
import com.aisino.common.util.excel.reader.XLSXCovertCSVReader;
import com.aisino.core.mybatis.DataSourceContextHolder;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import com.aisino.core.util.Delimiter;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfo, OrderInfoMapper> implements OrderInfoService {

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private AuthCodeInfoService authCodeInfoService;

    @Resource
    private EnInfoService enInfoService;

    @Autowired
    private GlobalInfoParams globalInfoParams;

    @Autowired
    private RequesttParams requesttParams;



    @Override
    protected void validateAddEntity(OrderInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        //设置订单状态默认为未开票
        if (StringUtils.isBlank(entity.getStatus()))
            entity.setStatus(OrderInfo.StatusType.NOTYET.getCode().toString());
    }


    /**
     * @return
     */
    @Override
    public void createRequestt(UserInfo userInfo, Integer orderInfoId) {
        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(userInfo.getUsrno());


        Requestt.Builder requesttBuilder = new Requestt.Builder();

        Requestt.GlobalInfo.Builder globalinfoBuilder = new Requestt.GlobalInfo.Builder();

        globalinfoBuilder.setAppId(globalInfoParams.getAppId());
        globalinfoBuilder.setResponseCode(globalInfoParams.getResponseCode());
        globalinfoBuilder.setTerminalCode(globalInfoParams.getTerminalCode());
        globalinfoBuilder.setVersion(globalInfoParams.getVersion());
        globalinfoBuilder.setInterfaceCode("DFXJ1009");
        globalinfoBuilder.setUserName(authCodeInfo.getPlatformCode());
        globalinfoBuilder.setRequestCode("P0000001");
        globalinfoBuilder.setPassWord("5600163257ixlV0FoSGORAjfsesyE+oQ==");
//        globalinfoBuilder.setPassWord(PasswordUtil.getPassword(authCodeInfo.getRegiCode()));
        globalinfoBuilder.setRequestTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SS"));
        globalinfoBuilder.setDataExchangeId(globalInfoParams.getDataExchangeId());
        globalinfoBuilder.setTaxpayerId(authCodeInfo.getTaxNo());
        globalinfoBuilder.setAuthorizationCode(authCodeInfo.getAuthCode());
        requesttBuilder.setGlobalInfo(globalinfoBuilder.build());

        Requestt.ReturnStateInfo.Builder returnStateInfoBuilder = new Requestt.ReturnStateInfo.Builder();
        returnStateInfoBuilder.setReturnCode("");
        returnStateInfoBuilder.setReturnMessage("");
        requesttBuilder.setReturnStateInfo(returnStateInfoBuilder.build());
        Requestt.Data.Builder dataBuilder = new Requestt.Data.Builder();


        Requestt.DataDescription.Builder dataDescriptionBuilder = new Requestt.DataDescription.Builder();
        dataDescriptionBuilder.setCodeType(requesttParams.getCodeType());
        dataDescriptionBuilder.setEncryptCode(requesttParams.getEncryptCode());
        dataDescriptionBuilder.setZipCode(requesttParams.getZipCode());

        dataBuilder.setDataDescription(dataDescriptionBuilder.build());
//        dataBuilder.setContent();
        KpRequestyl.RequestFpkjxx requestFpkjxx =  createKpRequestyl(userInfo, orderInfoId);

        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance(KpRequestyl.RequestFpkjxx.class);
            Marshaller ms = jc.createMarshaller();
            Writer w = new StringWriter();
            ms.marshal(requestFpkjxx,w);
            ms.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            dataBuilder.setContent(AesUtil.encrypt(w.toString()));



            requesttBuilder.setData(dataBuilder.build());


            Map<String, Object> map = new HashMap<String, Object>();

            jc = JAXBContext.newInstance(Requestt.class);
            Marshaller ms2 = jc.createMarshaller();
            Writer w2 = new StringWriter();
            ms2.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            ms2.marshal(requesttBuilder.build(),w2);
            map.put("xml",w2.toString());

            System.out.println(w2.toString());



            String url = "http://ei.szhtxx.com:9001/front/request/4458";//440300349724458
            String info = new HttpRequestor().doPost(url, map);
            System.out.println(info);
            //解析返回信息
            XStream xStream = new  XStream();
            xStream.alias("interface", KpInterface.class);
            KpInterface kpInterface = XmlHelper1.toBean(info, KpInterface.class);
            String returnCode = kpInterface.getReturnStateInfo().getReturnCode();
            System.out.println("returnCode:" + returnCode);
            System.out.println("returnMessage:"+ Base64.getFromBase64(kpInterface.getReturnStateInfo().getReturnMessage()));
            if (returnCode.equals("0000")) {
                System.out.println("content:"+kpInterface.getData().getContent());
                System.out.println("解密后content:" + AesUtil.decrypt(kpInterface.getData().getContent()));
            }


        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }





//        return requesttBuilder.build();

    }


    /**
     * @return
     */
    public KpRequestyl.RequestFpkjxx createKpRequestyl(UserInfo userInfo, Integer orderInfoId) {
        DataSourceContextHolder.user();
        OrderInfo orderInfo = this.findEntityById(orderInfoId);
        DataSourceContextHolder.write();
        if (orderInfo == null)
            return null;

        EnInfo enInfo = enInfoService.getByUsrno(userInfo.getUsrno());

        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(userInfo.getUsrno());

        KpRequestyl.RequestFpkjxx.Builder requestFpkjxxBuilder = new KpRequestyl.RequestFpkjxx.Builder();

        KpRequestyl.FpkjxxFptxx.Builder fpkjxxFptxxBuilder = new KpRequestyl.FpkjxxFptxx.Builder();
//        fpkjxxFptxxBuilder.setFpqqlsh();//发票请求唯一流水号
        fpkjxxFptxxBuilder.setDsptbm(authCodeInfo.getPlatformCode());//平台编码
        fpkjxxFptxxBuilder.setNsrsbh(enInfo.getTaxno());//开票方识别号
        fpkjxxFptxxBuilder.setNsrmc(enInfo.getTaxname());//开票方名称
        fpkjxxFptxxBuilder.setNsrdzdah(authCodeInfo.getElecNmber()); //开票方电子档案号
        fpkjxxFptxxBuilder.setSwjgd(authCodeInfo.getTaxCode()); //税务机构代码
        fpkjxxFptxxBuilder.setDkbz(orderInfo.getDkflags().toString()); //代开标志
        fpkjxxFptxxBuilder.setPydm(orderInfo.getTicketCode()); //票样代码
        fpkjxxFptxxBuilder.setKpxm(orderInfo.getMajorItems());   //主要开票项目
        fpkjxxFptxxBuilder.setBmbbbh(authCodeInfo.getCodeTableVer());  //编码表版本号
        fpkjxxFptxxBuilder.setXhfnsrsbh(enInfo.getTaxno()); //销货方识别号
        fpkjxxFptxxBuilder.setXhfmc(enInfo.getTaxname());   //销货方名称
        fpkjxxFptxxBuilder.setXhfdz(enInfo.getTaxaddr());  //销货方地址
        fpkjxxFptxxBuilder.setXhfdh(enInfo.getTaxtel());  //销货方电话
        fpkjxxFptxxBuilder.setXhfyhzh(enInfo.getBankaccount());  //销货方银行账号
        fpkjxxFptxxBuilder.setGhfmc(orderInfo.getBuyerName());  //购货方名称
        fpkjxxFptxxBuilder.setGhfnsrsbh(orderInfo.getBuyerTaxno());    //购货方识别号
        fpkjxxFptxxBuilder.setGhfsf(orderInfo.getBuyerProvince());  //购货方省份
        fpkjxxFptxxBuilder.setGhfdz(orderInfo.getBuyerAddr());  //购货方地址
        fpkjxxFptxxBuilder.setGhfgddh(orderInfo.getBuyerTele());  //购货方固定电话
        fpkjxxFptxxBuilder.setGhfsj(orderInfo.getBuyerMobile());  //购货方手机
        fpkjxxFptxxBuilder.setGhfemail(orderInfo.getBuyerEmail());  //购货方邮箱
        fpkjxxFptxxBuilder.setGhfqylx(orderInfo.getBuyerType());  //购货方企业类型
        fpkjxxFptxxBuilder.setGhfyhzh(orderInfo.getBuyerBankAcc());  //购货方银行账号
        fpkjxxFptxxBuilder.setHydm(orderInfo.getInduCode());  //行业代码
        fpkjxxFptxxBuilder.setHymc(orderInfo.getInduName());    //行业名称
        fpkjxxFptxxBuilder.setKpy(enInfo.getDrawer());  //开票员
        fpkjxxFptxxBuilder.setSky(enInfo.getPayee());  //收款员
        fpkjxxFptxxBuilder.setFhr(enInfo.getReviewer());  //复核人
//        fpkjxxFptxxBuilder.setKprq();  //开票日期  系统生成不用写
//        fpkjxxFptxxBuilder.setKplx();  //开票类型    1 正票 2 红票
//        fpkjxxFptxxBuilder.setYfpdm();  //原发票代码
//        fpkjxxFptxxBuilder.setYfphm();  //原发票号码
        /**
         *   10 正票正常开具
         *   11 正票错票重开
         *   20 退货折让红票、
         *   21 错票重开红票、
         *   22 换票冲红（全冲
         *   红电子发票，开具
         *   纸质发票）
         */
//        fpkjxxFptxxBuilder.setCzdm();  //操作代码
        /**
         * 0：根据项目名称字数，自动产生清单， 保持目前逻辑不变     1：取清单对应票面内容字段打印到发信息 XMXX 打印到清  单上。默认为 0。
         */
//        fpkjxxFptxxBuilder.setQdbz();  //清单标志
        /**
         * 需要打印清单时对 应发票票面项目名 称 清单标识（ QD_BZ） 为 1 时必填。 为 0 不进行   处理
         */
//        fpkjxxFptxxBuilder.setQdxmmc();  //清单发票项目名称
//        fpkjxxFptxxBuilder.setChyy();  //冲红原因 冲红时填写，由企 业定义
//        fpkjxxFptxxBuilder.setTschbz();  //特殊冲红标志
////        fpkjxxFptxxBuilder.setKphjje(orderInfo.get);  //价税合计金额 小数点后 元为单位精确到分 2 位，
//        fpkjxxFptxxBuilder.setHjbhsje();  //合计不含税金额 小数点后 2 位，以     元为单位精确到分（单行商品金额之   和）。 平台处理价税        分离，此值传 0
//        fpkjxxFptxxBuilder.setHjse();  //合计税额 小数点后 2 位，以     元为单位精确到分         (单行商品税额之    和)，平台处理价税      分离，此值传 0
//        fpkjxxFptxxBuilder.setBz();  //备注
//        fpkjxxFptxxBuilder.setByzd1();  //备用字段1
//        fpkjxxFptxxBuilder.setByzd2();  //备用字段2
//        fpkjxxFptxxBuilder.setByzd3();  //备用字段3
//        fpkjxxFptxxBuilder.setByzd4();  //备用字段4
//        fpkjxxFptxxBuilder.setByzd5();  //备用字段5

        requestFpkjxxBuilder.setFpkjxxFptxx(fpkjxxFptxxBuilder.build());

        KpRequestyl.FpkjxxXmxxs.Builder fpkjxxXmxxsBuilder = new KpRequestyl.FpkjxxXmxxs.Builder();

        DataSourceContextHolder.user();
        List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(orderInfo.getOrderNo());
        DataSourceContextHolder.write();
        List<KpRequestyl.FpkjxxXmxx> fpkjxxXmxxList = new ArrayList<>(orderDetails.size());

        for(OrderDetail orderDetail : orderDetails){
            KpRequestyl.FpkjxxXmxx.Builder fpkjxxXmxxBuilder = new KpRequestyl.FpkjxxXmxx.Builder();
            fpkjxxXmxxBuilder.setXmmc(orderDetail.getItemName());    //项目名称
            fpkjxxXmxxBuilder.setXmdw(orderDetail.getItemUnit());   //项目单位
            fpkjxxXmxxBuilder.setGgxh(orderDetail.getSpecMode());   //规格型号
            fpkjxxXmxxBuilder.setXmsl(orderDetail.getItemNum());   //项目数量
//            fpkjxxXmxxBuilder.setHsbz(orderDetail.get);   //含税标志
            fpkjxxXmxxBuilder.setXmdj(orderDetail.getItemPrice());   //项目单价
            fpkjxxXmxxBuilder.setFphxz(orderDetail.getInvoiceNature().toString());   //发票行性质
//            fpkjxxXmxxBuilder.setSpbm(orderDetail.get);   //商品编码
//            fpkjxxXmxxBuilder.setZxbm();   //自行编码
//            fpkjxxXmxxBuilder.setYhzcbs();   //优惠政策标识
//            fpkjxxXmxxBuilder.setLslbs();   //零税率标识
//            fpkjxxXmxxBuilder.setZzstsgl();   //增值税特殊管理
//            fpkjxxXmxxBuilder.setXmje();   //项目金额
            fpkjxxXmxxBuilder.setSl(orderDetail.getTaxRate());   //税率
//            fpkjxxXmxxBuilder.setSe();   //税额
//            fpkjxxXmxxBuilder.setByzd1();   //备用字段1
//            fpkjxxXmxxBuilder.setByzd2();   //备用字段2
//            fpkjxxXmxxBuilder.setByzd3();   //备用字段3
//            fpkjxxXmxxBuilder.setByzd4();   //备用字段4
//            fpkjxxXmxxBuilder.setByzd5();   //备用字段5

            fpkjxxXmxxList.add(fpkjxxXmxxBuilder.build());
        }


        fpkjxxXmxxsBuilder.setSize(fpkjxxXmxxList.size()+"");
//        fpkjxxXmxxsBuilder.setFpkjxxXmxxList(fpkjxxXmxxList);


        requestFpkjxxBuilder.setFpkjxxXmxxs(fpkjxxXmxxsBuilder.build());

        //项目信息
        KpRequestyl.FpkjxxDdxx.Builder fpkjxxDdxxBuilder = new KpRequestyl.FpkjxxDdxx.Builder();
        fpkjxxDdxxBuilder.setDdh(orderInfo.getOrderNo());   //订单号
//        fpkjxxDdxxBuilder.setThdh();  //退货单号
        fpkjxxDdxxBuilder.setDddate(DateFormatUtils.format(orderInfo.getOrderDate(),"yyyy-MM-dd HH:mm:ss SS"));  //订单日期

        requestFpkjxxBuilder.setFpkjxxDdxx(fpkjxxDdxxBuilder.build());

        //订单明细
        KpRequestyl.FpkjxxDdmxxxs.Builder fpkjxxDdmxxxsBuilder = new KpRequestyl.FpkjxxDdmxxxs.Builder();

        List<KpRequestyl.FpkjxxDdmxxx> fpkjxxDdmxxxList = new ArrayList<>();

        //(XMXX)项目信息（ 发票明细）（ 多条）
        KpRequestyl.FpkjxxDdmxxx.Builder fpkjxxDdmxxxBuilder = new KpRequestyl.FpkjxxDdmxxx.Builder();
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

        fpkjxxDdmxxxList.add(fpkjxxDdmxxxBuilder.build());



        fpkjxxDdmxxxsBuilder.setSize(fpkjxxDdmxxxList.size()+"");
//        fpkjxxDdmxxxsBuilder.setFpkjxxDdmxxxList(fpkjxxDdmxxxList);

        requestFpkjxxBuilder.setFpkjxxDdmxxxs(fpkjxxDdmxxxsBuilder.build());

        //支付信息
        KpRequestyl.FpkjxxZfxx.Builder fpkjxxZfxxBuilder = new KpRequestyl.FpkjxxZfxx.Builder();
//        fpkjxxZfxxBuilder.setZffs();    //支付方式
//        fpkjxxZfxxBuilder.setZflsh();   //支付流水号
//        fpkjxxZfxxBuilder.setZfpt();    //支付平台

        requestFpkjxxBuilder.setFpkjxxZfxx(fpkjxxZfxxBuilder.build());

        //物流信息
        KpRequestyl.FpkjxxWlxx.Builder fpkjxxWlxxBuilder = new KpRequestyl.FpkjxxWlxx.Builder();
//        fpkjxxWlxxBuilder.setCygs();   //承运公司
//        fpkjxxWlxxBuilder.setShsj();   //送货时间
//        fpkjxxWlxxBuilder.setWldh();   //物流单号
//        fpkjxxWlxxBuilder.setShdz();   //送货地址


        requestFpkjxxBuilder.setFpkjxxWlxx(fpkjxxWlxxBuilder.build());

        return requestFpkjxxBuilder.build();
    }


    /**
     * 读取Excel
     */
    @Override
    public ImportResultDto importExcel(InputStream inputStream) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        OPCPackage p = OPCPackage.open(inputStream);
        XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, "订单", 19);
        List<String[]> orderInfos = xlsx2csv.process();
        Map<String, OrderInfo> orderInfoMap = new HashMap<>();
        //不要表头
        for (int i = 1; i < orderInfos.size(); i++) {
            orderInfoMap.put(orderInfos.get(i)[16], orderInfoTransform(orderInfos.get(i)));
        }

        XLSXCovertCSVReader xlsx2csv2 = new XLSXCovertCSVReader(p, System.out, "明细", 8);
        List<String[]> orderdetails = xlsx2csv2.process();
        Map<String, List<OrderDetail>> orderDetailMap = new HashMap<>();
        //不要表头
        for (int i = 1; i < orderdetails.size(); i++) {
            if (orderDetailMap.get(orderdetails.get(i)[0]) == null) {
                List<OrderDetail> list = new ArrayList<>();
                list.add(orderDetailService.orderDetailTransform(orderdetails.get(i)));
                orderDetailMap.put(orderdetails.get(i)[0], list);
            } else {
                orderDetailMap.get(orderdetails.get(i)[0]).add(orderDetailService.orderDetailTransform(orderdetails.get(i)));
            }
        }
        p.close();


        /**
         * 保存到数据库
         */
        Iterator<String> orderInfoKeyIte = orderInfoMap.keySet().iterator();

        ImportResultDto importResultDto = new ImportResultDto(orderInfos.size(), orderdetails.size());//导入结果dto
        while (orderInfoKeyIte.hasNext()) {
            String orderNo = orderInfoKeyIte.next();
            if (orderDetailMap.get(orderNo) != null && !orderDetailMap.get(orderNo).isEmpty()) {

                /**
                 * 检查是否存在重复单号
                 */
                Specification<OrderInfo> specification = new Specification<>(OrderInfo.class);
                specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
                Long resultCount = this.findRowCount(specification);
                if (resultCount > 0) {
                    importResultDto.getRepeatOrderNo().add(orderNo);
                    continue;
                }

                this.addEntity(orderInfoMap.get(orderNo));
                orderInfoKeyIte.remove();//删除已经成功添加的主档
                for (OrderDetail orderDetail : orderDetailMap.get(orderNo)) {
                    orderDetailService.addEntity(orderDetail);
                }
                orderDetailMap.remove(orderNo);//删除已经成功添加的明细
            }
        }

        calculateImportResultDto(orderInfoMap, orderDetailMap, importResultDto);
        return importResultDto;
    }


    private void calculateImportResultDto(Map<String, OrderInfo> orderInfoMap, Map<String, List<OrderDetail>> orderDetailMap, ImportResultDto importResultDto) {
        //统计缺少明细的失败主档数量,不包括重复单号
        importResultDto.getOrderInfofaultQty().set(orderInfoMap.keySet().size());
        importResultDto.getOrderInfoSuccessQty().set(importResultDto.getOrderInfoTotal() - importResultDto.getOrderInfofaultQty().get());
        //统计缺少主档的失败明细
        for (String key : orderDetailMap.keySet()) {
            importResultDto.getDetailFaultQty().getAndAdd(orderDetailMap.get(key).size());
        }
        importResultDto.getDetailSuccessQty().set(importResultDto.getDetailTotal() - importResultDto.getDetailFaultQty().get());

        if (importResultDto.getOrderInfofaultQty().get() > 0) {
            String msg = "主档缺少明细,放弃导入,主档单号(部分):";
            int i = 0;
            for (String key : orderInfoMap.keySet()) {
                //如果不是重复单号的,就是缺少明细
                if (!importResultDto.getRepeatOrderNo().contains(key)) {
                    msg += key + Delimiter.COMMA.getDelimiter();
                    if (i == 10)
                        break;
                    i++;
                }
            }
            importResultDto.setOrderInfoDesc(msg);
        }

        if (importResultDto.getDetailFaultQty().get() > 0) {
            String msg = "明细缺少主档,放弃导入,明细单号(部分):";
            int i = 0;
            for (String key : orderDetailMap.keySet()) {
                if (!importResultDto.getRepeatOrderNo().contains(key)) {
                    msg += key + Delimiter.COMMA.getDelimiter();
                    if (i == 10)
                        break;
                    i++;
                }
            }
            importResultDto.setDetailDesc(msg);
        }

        if (importResultDto.getRepeatOrderNo().size() > 0) {
            String msg = "重复主档单号,放弃导入,单号(部分):";
            for (int i = 0; i < importResultDto.getRepeatOrderNo().size() && i < 10; i++) {
                msg += importResultDto.getRepeatOrderNo().get(i) + Delimiter.COMMA.getDelimiter();
            }
            importResultDto.setRepeatOrderNoDesc(msg);
        }


    }

    /**
     * 把数据根据EXCEL 模板顺序组装成实体
     *
     * @param value
     * @return
     */
    private OrderInfo orderInfoTransform(String[] value) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTaxno(StringUtils.trimToNull(value[0]));
        String dkflags = StringUtils.trimToNull(value[1]);
        if(dkflags!=null && StringUtils.isNumeric(dkflags) && OrderInfo.DkflagsType.getNameByCode(Integer.parseInt(dkflags)).length()>0)
        orderInfo.setDkflags(Integer.parseInt(dkflags));
        orderInfo.setTicketCode(StringUtils.trimToNull(value[2]));
        orderInfo.setMajorItems(StringUtils.trimToNull(value[3]));
        orderInfo.setBuyerName(StringUtils.trimToNull(value[4]));
        orderInfo.setBuyerTaxno(StringUtils.trimToNull(value[5]));
        orderInfo.setBuyerAddr(StringUtils.trimToNull(value[6]));
        orderInfo.setBuyerProvince(StringUtils.trimToNull(value[7]));
        orderInfo.setBuyerTele(StringUtils.trimToNull(value[8]));
        orderInfo.setBuyerMobile(StringUtils.trimToNull(value[9]));
        orderInfo.setBuyerEmail(StringUtils.trimToNull(value[10]));
        orderInfo.setBuyerType(StringUtils.trimToNull(value[11]));
        orderInfo.setBuyerBankAcc(StringUtils.trimToNull(value[12]));
        orderInfo.setInduCode(StringUtils.trimToNull(value[13]));
        orderInfo.setInduName(StringUtils.trimToNull(value[14]));
        orderInfo.setRemarks(StringUtils.trimToNull(value[15]));
        orderInfo.setOrderNo(StringUtils.trimToNull(value[16]));
        orderInfo.setUsrno(StringUtils.trimToNull(value[17]));

        String orderDateStr = StringUtils.trimToNull(value[18]);

        if(orderDateStr.indexOf("-")!=-1 && orderDateStr.indexOf(":")!=-1){
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr,"yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(orderDateStr.indexOf("/")!=-1 && orderDateStr.indexOf(":")!=-1){
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr,"yyyy/MM/dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(orderDateStr.indexOf("-")!=-1){
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr,"yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(orderDateStr.indexOf("/")!=-1){
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr,"yyyy/MM/dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return orderInfo;
    }


    //--------------------------导入结果类-----------------------------------


    public static class ImportResultDto {
        public ImportResultDto() {
            this.orderInfoSuccessQty = new AtomicInteger(0);
            this.orderInfofaultQty = new AtomicInteger(0);
            this.detailSuccessQty = new AtomicInteger(0);
            this.detailFaultQty = new AtomicInteger(0);
            this.repeatOrderNo = new ArrayList<>();
        }


        public ImportResultDto(int orderInfoTotal, int detailTotal) {
            this.orderInfoTotal = orderInfoTotal;
            this.detailTotal = detailTotal;
            this.orderInfoSuccessQty = new AtomicInteger(0);
            this.orderInfofaultQty = new AtomicInteger(0);
            this.detailSuccessQty = new AtomicInteger(0);
            this.detailFaultQty = new AtomicInteger(0);
            this.repeatOrderNo = new ArrayList<>();
        }

        /**
         * 导入订单总数
         */
        private int orderInfoTotal;

        /**
         * 导入明细总数
         */
        private int detailTotal;

        /**
         * 订单成功数量
         */
        private AtomicInteger orderInfoSuccessQty;

        /**
         * 订单失败数量
         */
        private AtomicInteger orderInfofaultQty;

        private List<String> repeatOrderNo;

        /**
         * 明细成功数量
         */
        private AtomicInteger detailSuccessQty;

        /**
         * 明细失败数量
         */
        private AtomicInteger detailFaultQty;

        /**
         * 主档描述信息
         */
        private String orderInfoDesc;

        /**
         * 明细描述信息
         */
        private String detailDesc;

        /**
         * 重复单号描述信息
         */
        private String repeatOrderNoDesc;

        //------------------------------getter and setter ---------------------------------------------


        public int getOrderInfoTotal() {
            return orderInfoTotal;
        }

        public void setOrderInfoTotal(int orderInfoTotal) {
            this.orderInfoTotal = orderInfoTotal;
        }

        public int getDetailTotal() {
            return detailTotal;
        }

        public void setDetailTotal(int detailTotal) {
            this.detailTotal = detailTotal;
        }

        public AtomicInteger getOrderInfoSuccessQty() {
            return orderInfoSuccessQty;
        }

        public void setOrderInfoSuccessQty(AtomicInteger orderInfoSuccessQty) {
            this.orderInfoSuccessQty = orderInfoSuccessQty;
        }

        public AtomicInteger getOrderInfofaultQty() {
            return orderInfofaultQty;
        }

        public void setOrderInfofaultQty(AtomicInteger orderInfofaultQty) {
            this.orderInfofaultQty = orderInfofaultQty;
        }

        public AtomicInteger getDetailSuccessQty() {
            return detailSuccessQty;
        }

        public void setDetailSuccessQty(AtomicInteger detailSuccessQty) {
            this.detailSuccessQty = detailSuccessQty;
        }

        public AtomicInteger getDetailFaultQty() {
            return detailFaultQty;
        }

        public void setDetailFaultQty(AtomicInteger detailFaultQty) {
            this.detailFaultQty = detailFaultQty;
        }

        public String getOrderInfoDesc() {
            return orderInfoDesc;
        }

        public void setOrderInfoDesc(String orderInfoDesc) {
            this.orderInfoDesc = orderInfoDesc;
        }

        public String getDetailDesc() {
            return detailDesc;
        }

        public void setDetailDesc(String detailDesc) {
            this.detailDesc = detailDesc;
        }

        public List<String> getRepeatOrderNo() {
            return repeatOrderNo;
        }

        public void setRepeatOrderNo(List<String> repeatOrderNo) {
            this.repeatOrderNo = repeatOrderNo;
        }

        public String getRepeatOrderNoDesc() {
            return repeatOrderNoDesc;
        }

        public void setRepeatOrderNoDesc(String repeatOrderNoDesc) {
            this.repeatOrderNoDesc = repeatOrderNoDesc;
        }
    }


}
