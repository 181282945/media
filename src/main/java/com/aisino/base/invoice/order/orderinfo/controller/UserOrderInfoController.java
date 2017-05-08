package com.aisino.base.invoice.order.orderinfo.controller;

import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.base.invoice.order.orderinfo.service.impl.OrderInfoServiceImpl;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.common.annotation.CuzDataSource;
import com.aisino.common.controller.IndexController;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import com.aisino.common.util.ParamUtil;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.security.util.SecurityUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by 为 on 2017-4-27.
 * 前台用户用的订单信息控制器
 */
@RestController
@RequestMapping(value = UserOrderInfoController.PATH)
@AclResc(id = 20000,code = "userOrderInfo", name = UserOrderInfoController.MODULE_NAME,homePage = UserOrderInfoController.HOME_PAGE,target = AclResource.Target.USERINFO)
public class UserOrderInfoController extends BaseController<OrderInfo> {

    final static String PATH = "/base/invoice/order/orderinfo/u";

    public final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "订单仓库";


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
        mav.addObject("userName", SecurityUtil.getCurrentUserName());
        mav.addObject("INDEX_HOME_PAGE", IndexController.HOME_PAGE);
        mav.addObject("USERORDERINFO_HOME_PAGE", UserOrderInfoController.HOME_PAGE);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        mav.addObject("orderInfoStatusParams", ParamUtil.JqgridSelectVal(OrderInfo.StatusType.getParams()));
        return mav;
    }


    /**
     * 用户订单查询列表
     */
    @CuzDataSource
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 20001,code = "list",name = "查询列表")
    @Transactional(readOnly = true)
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
    @AclResc(id = 20002,code = "add",name = "新增订单")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto add(@ModelAttribute("orderInfo")OrderInfo orderInfo){
        if(orderInfoService.addEntity(orderInfo)!=null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     */
    @CuzDataSource
    @RequestMapping(value = "/view",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 20003,code = "view",name = "查看")
    @Transactional(readOnly = true)
    public ResultDataDto view(@RequestParam("id")Integer id){
        return new ResultDataDto(orderInfoService.findEntityById(id));
    }

    /**
     * 更新
     */
    @CuzDataSource
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 20004,code = "update",name = "更新订单")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto update(@ModelAttribute("orderInfo")OrderInfo orderInfo){
        orderInfoService.updateEntity(orderInfo);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 上传导入订单
     */
    @CuzDataSource
    @RequestMapping(value = "/upload",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 20005,code = "upload",name = "上传订单")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto upload(HttpServletRequest request){
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        OrderInfoServiceImpl.ImportResultDto importResultDto = null;
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                    try {
                        importResultDto = orderInfoService.importExcel(file.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OpenXML4JException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ResultDataDto.addOperationSuccess().setDatas(importResultDto);
    }


    /**
     * 生效
     */
    @CuzDataSource
    @RequestMapping(value = "/effective",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 20006,code = "effective",name = "订单生效")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto effective(@RequestParam("id") Integer id){
        orderInfoService.updateEntityEffective(id);
        return ResultDataDto.addOperationSuccess();
    }

    /**
     * 失效
     */
    @CuzDataSource
    @RequestMapping(value = "/invalid",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 20007,code = "invalid",name = "订单失效")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultDataDto invalid(@RequestParam("id") Integer id){
        orderInfoService.updateEntityInvalid(id);
        return ResultDataDto.addOperationSuccess();
    }

}
