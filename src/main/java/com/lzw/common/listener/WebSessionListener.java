package com.lzw.common.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

/**
 * Created by 为 on 2017-5-5.
 */
@WebListener
public class WebSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    }
}

