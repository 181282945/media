package com.aisino.e9.controller.sysmgr.parameter;

import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import com.aisino.e9.service.sysmgr.parameter.ParameterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 为 on 2017-6-12.
 */
@RestController
@RequestMapping(path = ParameterController.PATH)
public class ParameterController extends BaseController {

    final static String PATH = "/sysmgr/parameter";


    @Resource
    private ParameterService parameterService;

    @GetMapping(path = "/getparameterbytypeid",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultDataDto getParameterByTypeId(@RequestParam("typeId")Integer typeId){
        List<Parameter> parameters = parameterService.findByTypeId(typeId);
        return new ResultDataDto(parameters);
    }

    @GetMapping(path = "/getparameteroptionsbytypeid",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultDataDto getParameterOptionsByTypeId(@RequestParam("typeId")Integer typeId){
        List<Parameter> parameters = parameterService.findByTypeId(typeId);
        return new ResultDataDto(parameters);
    }



}
