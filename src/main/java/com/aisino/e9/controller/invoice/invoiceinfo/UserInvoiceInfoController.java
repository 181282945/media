package com.aisino.e9.controller.invoice.invoiceinfo;

import com.aisino.common.controller.CenterController;
import com.aisino.core.util.LocalError;
import com.aisino.e9.controller.invoice.order.orderinfo.UserOrderInfoController;
import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import com.aisino.e9.entity.invoice.invoiceinfo.vo.InvoiceInfoVo;
import com.aisino.e9.service.invoice.invoiceinfo.InvoiceInfoService;
import com.aisino.e9.service.invoice.invoiceinfo.ManualInvoiceService;
import com.aisino.e9.service.invoice.invoiceinfo.impl.InvoiceInfoServiceImpl;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.invoiceinfo.impl.SimpleSplitMergeHelper;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.cloudinvoice.service.MailService;
import com.aisino.common.controller.IndexController;
import com.aisino.common.model.xml.impl.KpRequestyl;
import com.aisino.common.params.SystemParameter;
import com.aisino.common.util.CalendarUtil;
import com.aisino.common.util.IOUtil;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.util.Delimiter;
import com.aisino.e9.entity.invoice.invoiceinfo.vo.ManualInvoice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 为 on 2017-5-5.
 */
@RestController
@RequestMapping(path = UserInvoiceInfoController.PATH)
@AclResc(id = 3000, code = "userInvoiceInfo", name = UserInvoiceInfoController.MODULE_NAME, homePage = UserInvoiceInfoController.HOME_PAGE, target = AclResource.Target.USERINFO)
public class UserInvoiceInfoController extends BaseController{

    final static String PATH = "/base/invoice/invoiceinfo/u";

    public final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "发票管理";


    //页面模板路径
    private static final String VIEW_NAME = "/list_invoiceinfo";
    //修改更新
//    private static final String UPDATE_URL = PATH + "/update";
//    //新增
//    private static final String ADD_URL = PATH + "/add";
//    //删除
//    private static final String DELETE_URL = PATH + "/invalid";
    //查询
    private static final String SEARCH_URL = PATH + "/list";


    //发送邮件信号量
    final Semaphore emailSemaphore = new Semaphore(20);

    //下载PDF信号量
    final Semaphore downLoadPdfSemaphore = new Semaphore(20);


    //批量下载PDF信号量
    final Semaphore downLoadPdfBatchSemaphore = new Semaphore(20);

    @Resource
    private InvoiceInfoService invoiceInfoService;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private MailService mailService;

    @Resource
    private ManualInvoiceService manualInvoiceService;

    @Resource
    private SimpleSplitMergeHelper simpleSplitMergeHelper;

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Autowired
    private SystemParameter systemParameter;

    @GetMapping(path = "/tolist", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList() {
        if (!cuzSessionAttributes.eninfoCheck())
            return new ModelAndView(CenterController.ENINFO_PAGE);
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("currentUserInfo", cuzSessionAttributes.getUserInfo());
        mav.addObject("INDEX_HOME_PAGE", IndexController.HOME_PAGE);
        mav.addObject("USERORDERINFO_HOME_PAGE", UserOrderInfoController.HOME_PAGE);
        mav.addObject("USERINVOICEINFO_HOME_PAGE", UserInvoiceInfoController.HOME_PAGE);
        mav.addObject("USERCENTER_HOME_PAGE", CenterController.HOME_PAGE);
        mav.addObject("MODULE_NAME", MODULE_NAME);
//        mav.addObject("UPDATE_URL",UPDATE_URL);
//        mav.addObject("ADD_URL",ADD_URL);
//        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL", SEARCH_URL);
        mav.addObject("cuzSessionAttr", cuzSessionAttributes);
        mav.addObject("invoiceTypeParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.QUERY, InvoiceInfo.InvoiceType.getParams()));
        mav.addObject("invoiceStatusParams", ParamUtil.JqgridSelectVal(ParamUtil.FirstOption.VIEW, InvoiceInfo.Status.getParams()));
        mav.addObject("taxRateOptions", systemParameter.createTaxRateOption(ParamUtil.FirstOption.SELECT));
        return mav;
    }


    /**
     * 用户发票查询列表
     */
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3001, code = "list", name = "查询列表")
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto list(@ModelAttribute InvoiceInfoVo invoiceInfoVO, @ModelAttribute("pageAndSort") PageAndSort pageAndSort) {
        if (invoiceInfoVO != null && invoiceInfoVO.getEndDate() != null) {
            //根据系统需求而定,云开票需要包含查询条件最后一天
            invoiceInfoVO.setEndDate(CalendarUtil.getDayAgo(CalendarUtil.parseGregorianCalendar(invoiceInfoVO.getEndDate()), -1).getTime());
        }
        invoiceInfoVO.setDelflags(BaseInvoiceEntity.DelflagsType.NORMAL.getCode());
        List<InvoiceInfoVo> invoiceInfoVos = invoiceInfoService.findPageInvoiceInfo(invoiceInfoVO, pageAndSort);
        invoiceInfoService.fillUsrname(invoiceInfoVos);
        return new ResultDataDto(invoiceInfoVos, pageAndSort);
    }

    /**
     * 普通开票
     */
    @PostMapping(path = "/billing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3002, code = "billing", name = "开票")
    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public ResultDataDto billing(@RequestParam("orderIds") Integer[] orderIds) {
//        Map<String, String> map = new HashMap<>();//单号-错误信息
        LocalError.getMapMessage().get().clear();
        if (orderIds.length == 0)
            LocalError.getMapMessage().get().put("0", "请选择开票订单!");
        for (Integer orderId : orderIds) {
            OrderInfo orderInfo = orderInfoService.findEntityById(orderId,cuzSessionAttributes.getEnInfo().getShardingId());
            invoiceInfoService.requestBilling(orderInfo, InvoiceInfo.InvoiceType.NORMAL, KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL);
        }

        if (LocalError.getMapMessage().get().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : LocalError.getMapMessage().get().entrySet()) {
                stringBuilder.append(entry.getValue() + "</br>");
            }
            return ResultDataDto.addOperationSuccess("以下失败:</br>" + stringBuilder.toString());
        }
        return ResultDataDto.addOperationSuccess("开票成功!");
    }

    /**
     * 下载发票
     */
    @GetMapping(path = "/download", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    @AclResc(id = 3003, code = "download", name = "下载")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void download(@RequestParam("id") Integer id, HttpServletResponse response) {
        if (downLoadPdfSemaphore.tryAcquire()) {
            InvoiceInfoServiceImpl.DownLoadHelper downLoadHelper = invoiceInfoService.executeDownloadRequest(id);
            if (downLoadHelper.isSuccess()) {
                IOUtil.downLoadByUrl(downLoadHelper.getInvoiceInfo().getPdfUrl(), response, downLoadHelper.getInvoiceInfo().getSerialNo() + ".pdf");
                downLoadHelper.updateDownloadCount(invoiceInfoService,cuzSessionAttributes.getEnInfo().getShardingId());
                downLoadPdfSemaphore.release();
            } else {
                downLoadPdfSemaphore.release();
                throw new RuntimeException(downLoadHelper.getErrorMsg());
            }
        } else {
            throw new RuntimeException("服务器繁忙,请稍后再试!");
        }
    }

    /**
     * 冲红
     */
    @PostMapping(path = "/doRed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3004, code = "doRed", name = "冲红")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto doRed(@RequestParam("invoiceId") final Integer invoiceId, @RequestParam("reMarks") final String reMarks) {
        InvoiceInfo invoiceInfo = invoiceInfoService.findEntityById(invoiceId,cuzSessionAttributes.getEnInfo().getShardingId());
        final Map<String, String> map = new HashMap<>();//单号-错误信息

//        Integer redId = invoiceInfoService.doRed(invoiceInfo, KpRequestyl.FpkjxxFptxx.CzdmType.RETURN_RED, reMarks, map);
        if (invoiceInfoService.doRed(invoiceInfo, KpRequestyl.FpkjxxFptxx.CzdmType.RETURN_RED, reMarks)) {
            invoiceInfo.setRedflags(InvoiceInfo.RedflagsType.ALREADY.getCode());
            invoiceInfoService.updateEntity(invoiceInfo,cuzSessionAttributes.getEnInfo().getShardingId());
//            InvoiceInfo red = invoiceInfoService.findEntityById(redId);
//            red.setReMarks(reMarks);
//            invoiceInfoService.updateEntity(red);
        }
        if (map.get(invoiceInfo.getSerialNo()) != null) {
            return ResultDataDto.addOperationFailure(map.get(invoiceInfo.getSerialNo()));
        }
        return ResultDataDto.addOperationSuccess("冲红成功");
    }

    /**
     * 下载EXCEL 订单模板
     */
    @GetMapping(path = "/downloadTemplate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3005, code = "downloadTemplate", name = "下载EXCEL模板")
    public void downloadTemplate(HttpServletResponse response) {
        InputStream inputStream = this.getClass().getResourceAsStream("/excelTemplate/order_template.xlsx");
        IOUtil.downLoadFile(inputStream, "order_template.xlsx", response);
    }

    /**
     * 批量下载
     */
    @GetMapping(path = "/downloadPdfs", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @AclResc(id = 3006, code = "downloadPdfs", name = "批量下载发票")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void downloadFiles(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Integer[] ids) throws IOException {
        if (downLoadPdfBatchSemaphore.tryAcquire()) {
            if (ids.length == 1) {
                InvoiceInfoServiceImpl.DownLoadHelper downLoadHelper = invoiceInfoService.executeDownloadRequest(ids[0]);
                if (downLoadHelper.isSuccess()) {
                    IOUtil.downLoadByUrl(downLoadHelper.getInvoiceInfo().getPdfUrl(), response, downLoadHelper.getInvoiceInfo().getSerialNo() + ".pdf");
                    downLoadHelper.updateDownloadCount(invoiceInfoService,cuzSessionAttributes.getEnInfo().getShardingId());
                    downLoadPdfBatchSemaphore.release();
                } else {
                    downLoadPdfBatchSemaphore.release();
                    throw new RuntimeException(downLoadHelper.getErrorMsg());
                }
            } else {
                //响应头的设置
                response.reset();
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");

                //设置压缩包的名字
                //解决不同浏览器压缩包名字含有中文时乱码的问题
                String downloadName = System.nanoTime() + ".zip";
                String agent = request.getHeader("USER-AGENT");
                if (agent.contains("MSIE") || agent.contains("Trident")) {
                    downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
                } else {
                    downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
                }
                response.setHeader("Content-Disposition", "attachment;fileName=" + downloadName);

                //设置压缩流：直接写入response，实现边压缩边下载
                ZipOutputStream zipos = null;
                try {
                    zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
                    zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //循环将文件写入压缩流
                DataOutputStream os = null;
                for (int i = 0; i < ids.length; i++) {
                    InvoiceInfoServiceImpl.DownLoadHelper downLoadHelper = invoiceInfoService.executeDownloadRequest(ids[i]);
                    try {
                        URL url = new URL(downLoadHelper.getInvoiceInfo().getPdfUrl());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        if (!downLoadHelper.isSuccess()) {
                            zipos.putNextEntry(new ZipEntry(downLoadHelper.getInvoiceInfo().getSerialNo() + Delimiter.UNDERLINE.getDelimiter() + downLoadHelper.getErrorMsg() + ".pdf"));
                        } else if (conn.getResponseCode() == HttpStatus.OK.value()) {//添加ZipEntry，并ZipEntry中写入文件流
                            zipos.putNextEntry(new ZipEntry(downLoadHelper.getInvoiceInfo().getSerialNo() + ".pdf"));
                            os = new DataOutputStream(zipos);
                            byte[] b = new byte[100];
                            int length = 0;
                            while ((length = in.read(b)) != -1) {
                                os.write(b, 0, length);
                            }
                            in.close();
                            downLoadHelper.updateDownloadCount(invoiceInfoService,cuzSessionAttributes.getEnInfo().getShardingId());
                        } else {
                            zipos.putNextEntry(new ZipEntry(downLoadHelper.getInvoiceInfo().getSerialNo() + "_无效下载地址.pdf"));
                        }
                        zipos.closeEntry();
                    } catch (IOException e) {
                        zipos.putNextEntry(new ZipEntry(downLoadHelper.getInvoiceInfo().getSerialNo() + "_无效下载地址.pdf"));
                        zipos.closeEntry();
                    }
                }

                //关闭流
                if (os != null) {
                    os.flush();
                    os.close();
                }
                zipos.close();
                downLoadPdfBatchSemaphore.release();
            }
        } else {
            throw new RuntimeException("服务器繁忙,请稍后再试!");
        }
    }

    /**
     * 发送发票PDF 邮件
     */
    @PostMapping(path = "/sendInvoice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3008, code = "sendInvoice", name = "发送PDF邮件")
    @Transactional(readOnly = true)
    public ResultDataDto sendInvoice(@RequestParam("ids") Integer[] ids, @RequestParam("email") String email) throws IOException {

        if (emailSemaphore.tryAcquire()) {
            //设置压缩包的名字
            //解决不同浏览器压缩包名字含有中文时乱码的问题
            String downloadName = System.nanoTime() + ".zip";

            //设置压缩流：直接写入response，实现边压缩边下载
            ZipOutputStream zipos = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                zipos = new ZipOutputStream(new BufferedOutputStream(baos));
                zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
            } catch (Exception e) {
                e.printStackTrace();
            }

            //循环将文件写入压缩流
            DataOutputStream os = null;
            for (int i = 0; i < ids.length; i++) {
                InvoiceInfo invoiceInfo = invoiceInfoService.findEntityById(ids[i],cuzSessionAttributes.getEnInfo().getShardingId());
                if (invoiceInfo != null) {
                    try {
                        URL url = new URL(invoiceInfo.getPdfUrl());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //添加ZipEntry，并ZipEntry中写入文件流
                        if (conn.getResponseCode() == HttpStatus.OK.value()) {
                            InputStream in = new BufferedInputStream(conn.getInputStream());
                            zipos.putNextEntry(new ZipEntry(invoiceInfo.getSerialNo() + ".pdf"));
                            os = new DataOutputStream(zipos);
                            byte[] b = new byte[100];
                            int length = 0;
                            while ((length = in.read(b)) != -1) {
                                os.write(b, 0, length);
                            }
                            in.close();
                        } else {
                            zipos.putNextEntry(new ZipEntry(invoiceInfo.getSerialNo() + "_无效下载地址.pdf"));
                        }
                        zipos.closeEntry();
                    } catch (IOException e) {
                        zipos.putNextEntry(new ZipEntry(invoiceInfo.getSerialNo() + "_无效下载地址.pdf"));
                        zipos.closeEntry();
                    }
                }
            }

            //关闭流
            if (os != null) {
                os.flush();
                os.close();
            }
            zipos.close();

            mailService.sendAttachmentsMail(email, "电子发票", "请下载附件!", downloadName, new ByteArrayResource(baos.toByteArray()));
            emailSemaphore.release();
            return ResultDataDto.addOperationSuccess("邮件已发送");
        }

        return ResultDataDto.addOperationSuccess("服务器繁忙,请稍后再试!");
    }

    /**
     * 手动开票
     */
    @PostMapping(path = "/manualBilling", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3009, code = "manualBilling", name = "手动开票")
    public ResultDataDto billing(@RequestBody ManualInvoice manualInvoice) {
        LocalError.getMapMessage().get().clear();
        manualInvoiceService.addOrderByManualInvoice(manualInvoice);
        if (LocalError.getMapMessage().get().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : LocalError.getMapMessage().get().entrySet()) {
                stringBuilder.append(entry.getValue() + "</br>");
            }
            return  ResultDataDto.addOperationSuccess("以下失败:</br>" + stringBuilder.toString());
        }
        return ResultDataDto.addOperationSuccess("开票成功!");
    }

    @PostMapping(path = "/autoBilling", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3010, code = "autoBilling", name = "智能开票")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto autoBilling(@RequestParam("orderIds") Integer[] orderIds) {
        LocalError.getMapMessage().get().clear();
        if (orderIds.length == 0)
            LocalError.getMapMessage().get().put("0", "请选择开票订单!");
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (Integer orderId : orderIds) {
            OrderInfo orderInfo = orderInfoService.findEntityById(orderId,cuzSessionAttributes.getEnInfo().getShardingId());
            if(StringUtils.trimToNull(orderInfo.getBuyerTaxno())==null){
                throw new RuntimeException("不支持缺省购方税号!");
            }
            orderInfos.add(orderInfoService.findEntityById(orderId,cuzSessionAttributes.getEnInfo().getShardingId()));
        }
        Set<String> serialNoSet = simpleSplitMergeHelper.mergeAndSplit(orderInfos);
        if(serialNoSet.size()==0)
            return  ResultDataDto.addOperationSuccess("没有可开票的单据!");
        for (String serialNo : serialNoSet) {
            invoiceInfoService.autoBilling(serialNo, InvoiceInfo.InvoiceType.NORMAL, KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL);
        }
        if (LocalError.getMapMessage().get().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : LocalError.getMapMessage().get().keySet()) {
                stringBuilder.append(key+":"+LocalError.getMapMessage().get().get(key) + "</br>");
            }
            return  ResultDataDto.addOperationSuccess("以下失败:</br>" + stringBuilder.toString());
        }
        return  ResultDataDto.addOperationSuccess("开票成功!");
    }


    @PostMapping(path = "/autoBillingSingle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 3011, code = "autoBillingSingle", name = "单张智能开票")
    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public ResultDataDto autoBillingSingle(@RequestParam("invoiceId") Integer invoiceId) {
        LocalError.getMapMessage().get().clear();
        InvoiceInfo invoiceInfo = invoiceInfoService.findEntityById(invoiceId,cuzSessionAttributes.getEnInfo().getShardingId());
        invoiceInfoService.autoBilling(invoiceInfo.getSerialNo(), InvoiceInfo.InvoiceType.NORMAL, KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL);
        if(LocalError.getMapMessage().get().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : LocalError.getMapMessage().get().entrySet()) {
                stringBuilder.append(entry.getValue() + "</br>");
            }
           return ResultDataDto.addOperationFailure("以下失败:</br>" + stringBuilder.toString());
        }
        return ResultDataDto.addOperationSuccess("开票成功!");
    }
}
