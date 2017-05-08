package com.aisino;

import com.aisino.common.listener.WebSessionListener;
import com.aisino.common.params.SystemParameter;
import com.aisino.core.listener.StartupListener;
import com.aisino.core.util.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 *	服务启动类
 */
@SpringBootApplication
@ServletComponentScan(basePackageClasses = {StartupListener.class, WebSessionListener.class})
@EnableConfigurationProperties({SystemParameter.class})
public class Application extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {
	/**
	 * 自己new一个SpringApplication 为了添加监听器,实例调用run
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	/**
	 * 添加监听器
	 * @param servletContext
	 * @throws ServletException
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.addListener((StartupListener)SpringUtils.getBean("startupListener"));
	}


	/**
	 * 自定义端口
	 * @param container
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(80);
	}

	/**
	 * 配置外部TOMCAT
	 * @param application
	 * @return
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
