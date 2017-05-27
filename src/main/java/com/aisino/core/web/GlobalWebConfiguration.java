package com.aisino.core.web;


import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;

/**
 * Created by ZhenWeiLai on 2017/4/9.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class GlobalWebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("index");
        super.addViewControllers(registry);
    }



    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/images/kaptcha.jpg");
        servlet.addInitParameter("kaptcha.image.width", "160"/*kborder*/);//无边框
//        servlet.addInitParameter("kaptcha.session.key", "kaptcha.code");//session key
        servlet.addInitParameter("kaptcha.image.height", "50");
        servlet.addInitParameter("kaptcha.textproducer.char.string", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789");
        servlet.addInitParameter("kaptcha.textproducer.char.length", "4");
        servlet.addInitParameter("kaptcha.border", "no");
        servlet.addInitParameter("kaptcha.border.color", "black");
        servlet.addInitParameter("kaptcha.textproducer.font.size", "40");
        servlet.addInitParameter("kaptcha.textproducer.font.names", "Arial");
        servlet.addInitParameter("kaptcha.noise.color", "black");
        servlet.addInitParameter("kaptcha.textproducer.char.space", "3"); //和登录框背景颜色一致
        servlet.addInitParameter("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");//去掉干扰线
        servlet.addInitParameter("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        servlet.addInitParameter("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        return servlet;
    }
}
