package com.sunjung.base.sysmgr.aclresource.controller;

import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.common.dto.jqgrid.JqgridFilters;
import com.sunjung.common.dto.param.ParamDto;
import com.sunjung.common.params.Params;
import com.sunjung.common.util.ParamUtil;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import com.sunjung.core.mybatis.specification.PageAndSort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@RestController
@RequestMapping(value = AclResourceController.PATH)
@AclResc(code = "aclResource", name = AclResourceController.MODULE_NAME,homePage = AclResourceController.PATH + AclResourceController.HOME_PAGE)
public class AclResourceController extends BaseController<AclResource> {
    final static String PATH = "/base/sysmgr/aclresource";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "资源管理";

    @InitBinder("aclResource")
    public void initBinder1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("aclResource.");
    }

    @Resource
    private Params params;

    @Resource
    private AclResourceService aclResourceService;

    //页面模板路径
    private static final String VIEW = "/list_aclresource";
    //修改更新
    private static final String UPDATE_URL = "/base/sysmgr/aclresource/update";
    //新增
    private static final String ADD_URL = "/base/sysmgr/aclresource/add";
    //删除
    private static final String DELETE_URL = "/base/sysmgr/aclresource/delete";
    //查询
    private static final String SEARCH_URL = "/base/sysmgr/aclresource/list";

    /**
     * @return
     */
    @RequestMapping(value = AclResourceController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH + VIEW);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        mav.addObject("aclMenuParams",ParamUtil.JqgridSelectVal(Params.getAclMenuParams()));
        return mav;
    }

    /**
     * 查询方法
     * @param jqgridFilters
     * @param pageAndSort
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "list",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto list(JqgridFilters jqgridFilters, @ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclResource> aclResources = aclResourceService.findByJqgridFilters(jqgridFilters,pageAndSort);
        return new ResultDataDto(aclResources,pageAndSort);
    }

    /**
     * 更新
     * @param aclResource
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "add",name = "新增增资源")
    public ResultDataDto add(@ModelAttribute("aclResource")AclResource aclResource){
         if(aclResourceService.addEntity(aclResource)!=null)
             return ResultDataDto.addAddSuccess();
         return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 查询方法
     * @return
     */
    @RequestMapping(value = "/view",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "view",name = "查询列表")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto view(@RequestParam("id")Integer id){
        return new ResultDataDto(aclResourceService.findEntityById(id));
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "delete",name = "删除资源")
    public ResultDataDto delete(@RequestParam("id") Integer id){
        aclResourceService.deleteById(id);
        return ResultDataDto.addDeleteSuccess();
    }

    /**
     * 更新
     * @param aclResource
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "update",name = "更新资源")
    public ResultDataDto update(@ModelAttribute("aclResource")AclResource aclResource){
        aclResourceService.updateEntity(aclResource);
        return ResultDataDto.addUpdateSuccess();
    }
}
