package com.sunjung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;



/**
 *	服务启动类
 */
@SpringBootApplication
//@EnableConfigurationProperties({WechatProperties.class})
public class MediaApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {
	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
	}

	/**
	 *
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
		return application.sources(MediaApplication.class);
	}
}
