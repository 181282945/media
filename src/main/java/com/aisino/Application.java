package com.aisino;

import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.authcodeinfo.params.DataDescriptionParams;
import com.aisino.base.invoice.authcodeinfo.params.RequestParams;
import com.aisino.common.listener.WebSessionListener;
import com.aisino.common.params.SystemParameter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 *	服务启动类
 */
@SpringBootApplication
@ServletComponentScan(basePackageClasses = {WebSessionListener.class})
@EnableConfigurationProperties({SystemParameter.class, RequestParams.class, GlobalInfoParams.class, DataDescriptionParams.class})
public class Application extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {
	/**
	 * 自己new一个SpringApplication 为了添加监听器,实例调用run
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
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
