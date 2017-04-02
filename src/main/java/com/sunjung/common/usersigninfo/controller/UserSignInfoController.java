package com.sunjung.common.usersigninfo.controller;

import com.sunjung.common.usersigninfo.entity.UserSignInfo;
import com.sunjung.common.usersigninfo.service.UserSignInfoService;
import com.sunjung.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 */
@RestController
@RequestMapping("userSign")
public class UserSignInfoController extends BaseController {
    @Resource
    private UserSignInfoService userSignInfoService;

    @RequestMapping(value = "addTestData",method = RequestMethod.GET)
    public void addTestData(){
        for(int i=0;i<100;i++){
            UserSignInfo userSignInfo = new UserSignInfo();
            userSignInfo.setName("user"+i);
            userSignInfo.setPassword("pwd"+i);
            userSignInfo.setType("normal");
            userSignInfoService.addEntity(userSignInfo);
        }
    }
}
