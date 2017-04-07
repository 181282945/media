package com.sunjung.core.security.user.controller;

import com.sunjung.core.security.resource.annotation.Resc;
import com.sunjung.core.security.resource.entity.ResourceType;
import com.sunjung.core.security.resource.service.ResourceService;
import com.sunjung.core.security.user.entity.User;
import com.sunjung.core.security.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017/4/1.
 */
@RestController
@RequestMapping("/user")
@Resc(name="user",descn = "用户模块",resourceType = ResourceType.MODULE)
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private ResourceService resourceService;

    @RequestMapping("/initAdd")
    public void initAdd(){
        User user = new User();
//        user.setPassword("123456");
        user.setUsername("admin");
        userService.addEntity(user);
    }

    @RequestMapping("/aa")
    @Resc(name = "aa",descn = "测试aa",resourceType = ResourceType.METHOD)
    public void aa(){
        resourceService.findAll();
    }

}
