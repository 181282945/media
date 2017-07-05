package com.aisino.e9.controller.invoice.order.orderdetail;

import com.aisino.core.exception.ErrorMessageException;
import com.aisino.core.util.LocalError;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderdetail.vo.OrderDetailVo;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.controller.IndexController;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * Created by 为 on 2017-5-3.
 */
@RestController
@RequestMapping(path = UserOrderDetailController.PATH)
@AclResc(id = 5000,code = "userOrderDetail", name = UserOrderDetailController.MODULE_NAME,homePage = UserOrderDetailController.HOME_PAGE,target = AclResource.Target.USERINFO)
public class UserOrderDetailController extends BaseController {

    final static String PATH = "/base/invoice/order/orderdetail/u";

    public final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "订单明细";


    //页面模板路径
    private static final String VIEW_NAME = "/list_userorderdetail";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/invalid";
    //查询
    private static final String SEARCH_URL = PATH + "/list";

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @GetMapping(path = "/tolist",produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("userName", cuzSessionAttributes.getUserInfo().getUsrname());
        mav.addObject("INDEX_HOME_PAGE", IndexController.HOME_PAGE);
        mav.addObject("USERORDERINFO_HOME_PAGE", UserOrderDetailController.HOME_PAGE);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        return mav;
    }


    /**
     * 新增
     */
    @PostMapping(path = "/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5002,code = "add",name = "新增订单明细")
    public ResultDataDto add(@ModelAttribute("orderDetail")OrderDetail orderDetail){
        if(orderDetailService.addEntity(orderDetail)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     */
    @PostMapping(path = "/view",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5003,code = "view",name = "明细")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto view(@RequestParam("id")Integer id){
        return new ResultDataDto(orderDetailService.findEntityById(id,cuzSessionAttributes.getEnInfo().getShardingId()));
    }

    /**
     * 更新
     */
    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5004,code = "update",name = "更新订单明细")
    public ResultDataDto update(@ModelAttribute("orderDetail")OrderDetail orderDetail){
        orderDetailService.updateEntity(orderDetail,cuzSessionAttributes.getEnInfo().getShardingId());
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 根据订单号查询
     */
    @GetMapping(path = "/listByOrderInfoId",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5005,code = "listByOrderInfoId",name = "根据订单号查询")
    @Transactional(readOnly = true)
    public ResultDataDto listByOrderInfoId(@RequestParam("orderInfoId")Integer orderInfoId, @ModelAttribute PageAndSort pageAndSort){
        OrderInfo orderInfo = orderInfoService.findEntityById(orderInfoId,cuzSessionAttributes.getEnInfo().getShardingId());
        List<OrderDetailVo> orderDetailVos = orderDetailService.findOrderDetailVoPage(orderInfo.getOrderNo(),pageAndSort);
        return new ResultDataDto(orderDetailVos,pageAndSort);
    }



    /**
     * 上传导入订单
     */
    @PostMapping(path = "/upload",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 5006, code = "upload", name = "上传订单")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto upload(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
//        OrderDetailServiceImpl.ImportResultDto importResultDto = null;
        // 检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            // 将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 获取multiRequest 中所有的文件名
            Iterator<String> iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                // 一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    try {
                        LocalError.getMessage().get().clear();
                        orderDetailService.importAndSave(file.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    } catch (OpenXML4JException e) {
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    } catch (SAXException e) {
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    } catch (ErrorMessageException e) {
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        Collections.reverse(LocalError.getMessage().get());
                        return ResultDataDto.addOperationFailure().setDatas(LocalError.getMessage().get());
                    }
                }
            }
        }
        return ResultDataDto.addOperationSuccess();
    }

}
