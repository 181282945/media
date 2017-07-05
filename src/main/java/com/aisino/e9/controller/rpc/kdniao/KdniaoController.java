package com.aisino.e9.controller.rpc.kdniao;

import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.e9.service.rpc.kdniao.KdniaoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-6-12.
 */
@RestController
@RequestMapping(path = KdniaoController.PATH)
public class KdniaoController extends BaseController {

    @Resource
    private KdniaoService kdniaoService;

    final static String PATH = "/rpc/kdniao";

    /**
     *  根据订单号,订单公司编码查询订单轨迹
     */
    @GetMapping(path = "/getOrderTracesByJson",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultDataDto getOrderTracesByJson(String expCode, String expNo){
        try {
            String resultJson = kdniaoService.getOrderTracesByJson(expCode,expNo);
            return ResultDataDto.addOperationSuccess().setDatas(resultJson);
        } catch (Exception e) {
            return ResultDataDto.addOperationFailure("查询失败!");
        }
    }

}
