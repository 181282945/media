package com.aisino.e9.controller.rpc.requestfpkccx;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.model.xml.impl.RequestFpkccx;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.e9.service.rpc.requestfpkccx.RequestFpkccxService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-6-8.
 */
@RestController
@RequestMapping(path = RequestFpkccxController.PATH)
public class RequestFpkccxController extends BaseController {

    final static String PATH = "/requestfpkccx";

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private RequestFpkccxService requestFpkccxService;

    @GetMapping(path = "/getcnt",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultDataDto getCnt(@ModelAttribute("requestFpkccx")RequestFpkccx requestFpkccx){
        requestFpkccx.setNsrsbh(cuzSessionAttributes.getEnInfo().getTaxno());
        String cnt = requestFpkccxService.requestCnt(requestFpkccx);
        return ResultDataDto.addOperationSuccess().setDatas(cnt);
    }

}
