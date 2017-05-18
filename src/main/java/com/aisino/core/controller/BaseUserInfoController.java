package com.aisino.core.controller;

import com.aisino.core.entity.BaseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by 为 on 2017-5-17.
 * 前台用户Controller 必须继承此 抽象类
 */
public abstract class BaseUserInfoController<T extends BaseEntity> extends BaseController<T> {

//    @ModelAttribute
//    public void checkDbExist(HttpSession session) throws IOException {
//        boolean dbExist = (boolean)session.getAttribute("dbExist");
//        if(!dbExist)
//            throw new RuntimeException("数据库不存在,请联系管理员!");
//    }
}
