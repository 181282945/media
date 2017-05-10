package com.aisino.base.invoice.order.orderinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.authcodeinfo.params.RequesttParams;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.base.invoice.invoiceinfo.service.InvoiceInfoService;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
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
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
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

    @Resource
    private InvoiceInfoService invoiceInfoService;

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
    public void createRequestt(UserInfo userInfo, Integer orderInfoId, InvoiceInfo.InvoiceType invoiceType) {
        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(userInfo.getUsrno());


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
        dataDescription.setCodeType(requesttParams.getCodeType());
        dataDescription.setEncryptCode(requesttParams.getEncryptCode());
        dataDescription.setZipCode(requesttParams.getZipCode());

        data.setDataDescription(dataDescription);
        KpRequestyl.RequestFpkjxx requestFpkjxx = createKpRequestyl(userInfo, orderInfoId, invoiceType);

        JAXBContext jc;
        Writer writer = null;

        try {
            jc = JAXBContext.newInstance(KpRequestyl.RequestFpkjxx.class);
            Marshaller ms = jc.createMarshaller();
            writer = new StringWriter();
            ms.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
            ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
            ms.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xm头声明信息
            ms.marshal(requestFpkjxx, writer);
            data.setContent(AesUtil.encrypt(writer.toString()));
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        requestt.setData(data);


        Map<String, Object> map = new HashMap<String, Object>();


        try {
            jc = JAXBContext.newInstance(Requestt.class);
            Marshaller ms2 = jc.createMarshaller();
            writer = new StringWriter();
            ms2.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
            ms2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
//            ms2.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xm头声明信息
            ms2.marshal(requestt, writer);
            map.put("xml", writer.toString());
            String url = "http://ei.szhtxx.com:9001/front/request/4458";//440300349724458
            String info = new HttpRequestor().doPost(url, map);


            JAXBContext context = JAXBContext.newInstance(Requestt.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Requestt requestt2 = (Requestt) unmarshaller.unmarshal(new StringReader(info));

            //解析返回信息
            String returnCode = requestt2.getReturnStateInfo().getReturnCode();

            //如果成功返回信息,保存发票信息
            if (returnCode.equals("0000")) {
                String decrptContent = AesUtil.decrypt(requestt2.getData().getContent());
                invoiceInfoService.addByResponseFpkj(decrptContent, invoiceType, userInfo.getUsrno());
            }else {
                throw new RuntimeException(requestt2.getReturnStateInfo().getReturnMessage());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * @return
     */
    public KpRequestyl.RequestFpkjxx createKpRequestyl(UserInfo userInfo, Integer orderInfoId, InvoiceInfo.InvoiceType invoiceType) {
        DataSourceContextHolder.user();
        OrderInfo orderInfo = this.findEntityById(orderInfoId);
        DataSourceContextHolder.write();
        if (orderInfo == null)
            return null;

        EnInfo enInfo = enInfoService.getByUsrno(userInfo.getUsrno());

        AuthCodeInfo authCodeInfo = authCodeInfoService.getByUsrno(userInfo.getUsrno());

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
        if (dkflags != null && StringUtils.isNumeric(dkflags) && OrderInfo.DkflagsType.getNameByCode(Integer.parseInt(dkflags)).length() > 0)
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

        if (orderDateStr.indexOf("-") != -1 && orderDateStr.indexOf(":") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (orderDateStr.indexOf("/") != -1 && orderDateStr.indexOf(":") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy/MM/dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (orderDateStr.indexOf("-") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (orderDateStr.indexOf("/") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy/MM/dd"));
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
