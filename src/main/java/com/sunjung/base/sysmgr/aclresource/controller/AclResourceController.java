package com.sunjung.base.sysmgr.aclresource.controller;

import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import com.sunjung.core.mybatis.specification.PageAndSort;
import org.springframework.http.MediaType;
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
    private AclResourceService aclResourceService;

    @RequestMapping(value = AclResourceController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toList(){
        ModelAndView mav = new ModelAndView(PATH+"/list_aclresource");
        mav.addObject("MODULE_NAME",MODULE_NAME);

        return mav;
    }


    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "list",name = "查询列表")
    public ResultDataDto list(@ModelAttribute("aclResource")AclResource aclResource,@ModelAttribute("pageAndSort")PageAndSort pageAndSort){
        List<AclResource> aclResources = aclResourceService.findByPage(aclResource,pageAndSort);
        ResultDataDto resultDataDto =new ResultDataDto(aclResources,pageAndSort);
        System.out.println();
        return resultDataDto;
    }

    @RequestMapping(value = "/getJson",method = RequestMethod.GET)
    public AclResource getJson(){
        return new AclResource();
    }

}
