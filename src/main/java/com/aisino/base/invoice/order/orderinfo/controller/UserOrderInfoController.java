package com.aisino.base.invoice.order.orderinfo.controller;

import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;
import com.aisino.common.annotation.CuzDataSource;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.DataSourceContextHolder;
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
 * Created by 为 on 2017-4-27.
 * 前台用户用的订单信息控制器
 */
@RestController
@RequestMapping(value = UserOrderInfoController.PATH)
@AclResc(code = "userOrderInfo", name = UserOrderInfoController.MODULE_NAME,homePage = UserOrderInfoController.HOME_PAGE,target = AclResourceTarget.USERINFO)
public class UserOrderInfoController extends BaseController<OrderInfo> {

    final static String PATH = "/base/invoice/order/orderinfo/u";

    public final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "订单信息";


    //页面模板路径
    private static final String VIEW_NAME = "/list_userorderinfo";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/invalid";
    //查询
    private static final String SEARCH_URL = PATH + "/list";

    @Resource
    private OrderInfoService orderInfoService;


    @RequestMapping(value = "/tolist",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
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
    @AclResc(code = "list",name = "查询列表")
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<OrderInfo> orderInfos = orderInfoService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(orderInfos,pageAndSort);
    }


    /**
     * 新增
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增订单")
    public ResultDataDto add(@ModelAttribute("orderInfo")OrderInfo orderInfo){
        if(orderInfoService.addEntity(orderInfo)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     */
    @RequestMapping(value = "/view",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "view",name = "明细")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto view(@RequestParam("id")Integer id){
        return new ResultDataDto(orderInfoService.findEntityById(id));
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新资源")
    public ResultDataDto update(@ModelAttribute("orderInfo")OrderInfo orderInfo){
        orderInfoService.updateEntity(orderInfo);
        return ResultDataDto.addUpdateSuccess();
    }


    /**
     * 生效
     */
    @RequestMapping(value = "/effective",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "effective",name = "订单生效")
    public ResultDataDto effective(@RequestParam("id") Integer id){
        orderInfoService.updateEntityEffective(id);
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 失效
     */
    @RequestMapping(value = "/invalid",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "invalid",name = "订单失效")
    public ResultDataDto invalid(@RequestParam("id") Integer id){
        orderInfoService.updateEntityInvalid(id);
        return ResultDataDto.addOperationSuccess();
    }

}
