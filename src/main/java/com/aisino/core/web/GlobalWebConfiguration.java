package com.aisino.core.web;


import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;

/**
 * Created by ZhenWeiLai on 2017/4/9.
 */
@Configuration
public class GlobalWebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        super.addViewControllers(registry);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/images/kaptcha.jpg");
        servlet.addInitParameter("kaptcha.image.width", "90"/*kborder*/);//无边框
//        servlet.addInitParameter("kaptcha.session.key", "kaptcha.code");//session key
        servlet.addInitParameter("kaptcha.image.height", "40");
        servlet.addInitParameter("kaptcha.textproducer.char.string", "123456789abcDeFGHjkLmnoQqrsTUVWXYZ");
        servlet.addInitParameter("kaptcha.textproducer.char.length", "4");
        servlet.addInitParameter("kaptcha.border", "no");
        servlet.addInitParameter("kaptcha.border.color", "black");
        servlet.addInitParameter("kaptcha.textproducer.font.size", "30");
        servlet.addInitParameter("kaptcha.textproducer.font.names", "楷体");
        servlet.addInitParameter("kaptcha.noise.color", "black");
        servlet.addInitParameter("kaptcha.textproducer.char.space", "3"); //和登录框背景颜色一致
        servlet.addInitParameter("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");//去掉干扰线
        servlet.addInitParameter("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        return servlet;
    }
}
