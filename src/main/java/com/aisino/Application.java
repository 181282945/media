package com.aisino;

import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.authcodeinfo.params.DataDescriptionParams;
import com.aisino.base.invoice.authcodeinfo.params.RequestParams;
import com.aisino.common.params.Kdniao;
import com.aisino.common.params.SystemParameter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


/**
 *	服务启动类
 */
@SpringBootApplication
//@ServletComponentScan(basePackageClasses = {})
@EnableConfigurationProperties({SystemParameter.class, RequestParams.class, GlobalInfoParams.class, DataDescriptionParams.class, Kdniao.class})
public class Application{
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
//	@Override
//	public void customize(ConfigurableEmbeddedServletContainer container) {
//		container.setSessionTimeout(600);//单位为S
//		container.setPort(80);
//	}

	/**
	 * 配置外部TOMCAT
	 * @param application
	 * @return
	 */
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(Application.class);
//	}
}
