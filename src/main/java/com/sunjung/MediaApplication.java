package com.sunjung;

import com.sunjung.core.properties.WechatProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 *	服务启动类
 */
@SpringBootApplication
//@EnableConfigurationProperties({WechatProperties.class})
public class MediaApplication implements EmbeddedServletContainerCustomizer {
	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(80);
	}
}
