package com.aisino.common.listener;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.core.mybatis.MyRoutingDataSource;
import com.aisino.core.security.util.SecurityUtil;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

/**
 * Created by 为 on 2017-5-5.
 */
@WebListener
public class WebSessionListener implements HttpSessionListener {

    @Resource
    private MyRoutingDataSource routingDataSource;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        UserInfo userInfo = SecurityUtil.getCurrentUserInfo();
        if (userInfo != null)
            routingDataSource.removeCuzDataSource(userInfo);
    }
}

