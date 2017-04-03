package com.sunjung.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ZhenWeiLai on 2017/4/3.
 */
@RestController
@RequestMapping("/auth")
public class AuthTestController {

    @RequestMapping("/test")
    public void test(){
        System.out.println("gogoggo");
    }
}
