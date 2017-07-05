package com.aisino.e9.controller.sysmgr.expressinfo;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.e9.entity.expressinfo.pojo.ExpressInfo;
import com.aisino.e9.service.rpc.kdniao.KdniaoService;
import com.aisino.e9.service.sysmgr.expressinfo.ExpressInfoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-6-12.
 */
@RestController
@RequestMapping(path = ExpressInfoController.PATH)
@AclResc(id = 800, code = "expressInfo", name = ExpressInfoController.MODULE_NAME, homePage = ExpressInfoController.HOME_PAGE, target = AclResource.Target.USERINFO)
public class ExpressInfoController extends BaseController {


    final static String MODULE_NAME = "金税盘快递信息";

    final static String PATH = "/sysmgr/expressinfo";

    public final static String HOME_PAGE = PATH + "/tolist";

    @Resource
    private ExpressInfoService expressInfoService;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private KdniaoService kdniaoService;


    //根据快递单号,快递公司编码查询快递轨迹
    @GetMapping(path = "/getOrderTraces",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 802, code = "getOrderTraces", name = "查询快递信息")
    public ResultDataDto getOrderTraces(@RequestParam("expCode")String expCode,@RequestParam("expNo")String expNo){
        try {
            String result = kdniaoService.getOrderTracesByJson(expCode,expNo);
            return ResultDataDto.addOperationSuccess().setDatas(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDataDto.addOperationFailure("查询失败!");
        }
    }

    //用户新增快递信息
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 801, code = "add", name = "新增快递信息")
    public ResultDataDto add(@ModelAttribute("expressInfo") ExpressInfo expressInfo) {
        expressInfo.setUserId(cuzSessionAttributes.getUserInfo().getId());
        expressInfo.setEninfoId(cuzSessionAttributes.getEnInfo().getId());
        if (expressInfoService.addEntity(expressInfo) != null)
            return ResultDataDto.addAddSuccess();
        return ResultDataDto.addOperationSuccess();
    }

}
