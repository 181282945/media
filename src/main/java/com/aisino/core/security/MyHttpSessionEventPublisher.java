package com.aisino.core.security;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.core.mybatis.MyRoutingDataSource;
import com.aisino.core.util.SpringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import java.util.List;

/**
 * Created by 为 on 2017-5-23.
 */
public class MyHttpSessionEventPublisher extends HttpSessionEventPublisher {

    public final Integer SESSION_TIMEOUT_IN_SECONDS = 60 * 10;

    private MyRoutingDataSource routingDataSource;

    private UserInfoService userInfoService;

    private static final String LOGGER_NAME = MyHttpSessionEventPublisher.class.getName();

    public MyHttpSessionEventPublisher() {
        super();
    }


    ApplicationContext getContext(ServletContext servletContext) {
        return SecurityWebApplicationContextUtils.findRequiredWebApplicationContext(servletContext);
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSessionCreatedEvent e = new HttpSessionCreatedEvent(event.getSession());
        e.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
        Log log = LogFactory.getLog(LOGGER_NAME);
        if (log.isDebugEnabled()) {
            log.debug("Publishing event: " + e);
        }

        this.getContext(event.getSession().getServletContext()).publishEvent(e);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSessionDestroyedEvent e = new HttpSessionDestroyedEvent(event.getSession());
        List<SecurityContext> list = e.getSecurityContexts();
        if (list != null && !list.isEmpty()) {
            String username = ((UserDetails) list.get(0).getAuthentication().getPrincipal()).getUsername();
            UserInfo userInfo = getUserInfoService().getUserByUsrno(username);
            if (userInfo != null)
                getRoutingDataSource().removeCuzDataSource(userInfo);
        }

        Log log = LogFactory.getLog(LOGGER_NAME);
        if (log.isDebugEnabled()) {
            log.debug("Publishing event: " + e);
        }

        this.getContext(event.getSession().getServletContext()).publishEvent(e);
    }


    private MyRoutingDataSource getRoutingDataSource() {
        if (routingDataSource == null)
            routingDataSource = (MyRoutingDataSource) SpringUtils.getBean("routingDataSource");
        return routingDataSource;
    }

    private UserInfoService getUserInfoService() {
        if (userInfoService == null)
            userInfoService = (UserInfoService) SpringUtils.getBean("userInfoService");
        return userInfoService;
    }

}
