package com.sunjung.base.sysmgr.aclauth.controller;

import com.sunjung.base.sysmgr.aclauth.entity.AclAuth;
import com.sunjung.base.sysmgr.aclauth.service.AclAuthService;
import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-20.
 */
@RestController
@RequestMapping(value = AclAuthController.PATH)
@AclResc(code = "aclAuth", name = AclAuthController.MODULE_NAME,homePage = AclAuthController.PATH + AclAuthController.HOME_PAGE)
public class AclAuthController extends BaseController<AclAuth> {
    final static String PATH = "/base/sysmgr/aclauth";
    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "权限管理";

    @Resource
    private AclAuthService aclAuthService;

    @Resource
    private AclResourceService aclResourceService;

    //页面模板路径
    private static final String VIEW_NAME = "/list_aclauth";
    //修改更新
    private static final String UPDATE_URL = PATH + "/update";
    //新增
    private static final String ADD_URL = PATH + "/add";
    //删除
    private static final String DELETE_URL = PATH + "/delete";
    //查询 模块
    private static final String SEARCH_URL = PATH + "/list";


    /**
     * @return
     */
    @RequestMapping(value = AclAuthController.HOME_PAGE,method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
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
     * 根据资源ID新增
     */
    @RequestMapping(value = "/addByRescId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "addByRescId",name = "新增方法权限控制")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto addByRescId(@ModelAttribute("methodRescId")Integer methodRescId){
        AclResource aclResource = aclResourceService.findEntityById(methodRescId);
        if(!AclResourceType.METHOD.getCode().equals(aclResource.getType()))
            ResultDataDto.addOperationFailure("只有方法资源才能限定权限!");
        AclAuth aclAuth = new AclAuth();
        aclAuth.setCode(aclResource.getCode());
        aclAuth.setResourceId(aclResource.getId());
        if(aclAuthService.addEntity(aclAuth) != null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }

    /**
     * 根据资源ID新增
     */
    @RequestMapping(value = "/deleteByRescId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "deleteByRescId",name = "删除方法权限控制")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResultDataDto deleteByRescId(@ModelAttribute("methodRescId")Integer methodRescId){
        AclResource aclResource = aclResourceService.findEntityById(methodRescId);
        if(aclAuthService.deleteByRescId(aclResource.getId()) > 0)
            return ResultDataDto.addDeleteSuccess();
        return ResultDataDto.addOperationFailure("删除失败!");
    }

}
