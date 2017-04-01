package com.sunjung.core.security.controller;

import com.sunjung.core.security.entity.User;
import com.sunjung.core.security.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017/4/1.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/initAdd")
    public void initAdd(){
        User user = new User();
        user.setPassword("123456");
        user.setUsername("admin");
        userService.addEntity(user);
    }

}
