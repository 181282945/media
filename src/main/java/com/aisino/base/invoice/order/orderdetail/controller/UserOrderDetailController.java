package com.aisino.base.invoice.order.orderdetail.controller;

import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.annotation.CuzDataSource;
import com.aisino.common.controller.IndexController;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-5-3.
 */
@RestController
@RequestMapping(value = UserOrderDetailController.PATH)
@AclResc(id = 50000,code = "userOrderDetail", name = UserOrderDetailController.MODULE_NAME,homePage = UserOrderDetailController.HOME_PAGE,target = AclResource.Target.USERINFO)
public class UserOrderDetailController extends BaseController<OrderDetail> {

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


    @RequestMapping(value = "/tolist",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("userName", SecurityUtil.getCurrentUserName());
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
     * 用户订单查询列表
     */
    @CuzDataSource
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 50001,code = "list",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        jqgridFilters.getRules().add(new JqgridFilters.Rule("delflags", QueryLike.LikeMode.Eq.getCode(), BaseInvoiceEntity.DelflagsType.NORMAL.getCode()));
        List<OrderInfo> orderInfos = orderInfoService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(orderInfos,pageAndSort);
    }


    /**
     * 新增
     */
    @CuzDataSource
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 50002,code = "add",name = "新增订单明细")
    public ResultDataDto add(@ModelAttribute("orderDetail")OrderDetail orderDetail){
        if(orderDetailService.addEntity(orderDetail)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     */
    @CuzDataSource
    @RequestMapping(value = "/view",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 50003,code = "view",name = "明细")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto view(@RequestParam("id")Integer id){
        return new ResultDataDto(orderDetailService.findEntityById(id));
    }

    /**
     * 更新
     */
    @CuzDataSource
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 50004,code = "update",name = "更新订单明细")
    public ResultDataDto update(@ModelAttribute("orderDetail")OrderDetail orderDetail){
        orderDetailService.updateEntity(orderDetail);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 根据订单号查询
     */
    @CuzDataSource
    @RequestMapping(value = "/listByOrderInfoId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 50005,code = "listByOrderInfoId",name = "根据订单号查询")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto listByOrderInfoId(@RequestParam("orderInfoId")Integer orderInfoId, @ModelAttribute PageAndSort pageAndSort){
        OrderInfo orderInfo = orderInfoService.findEntityById(orderInfoId);
        List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(orderInfo.getOrderNo(),pageAndSort);
        return new ResultDataDto(orderDetails,pageAndSort);
    }

}
