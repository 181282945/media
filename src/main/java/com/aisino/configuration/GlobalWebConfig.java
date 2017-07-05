package com.aisino.configuration;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/9.
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
public class GlobalWebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/reg").setViewName("/reg");
        registry.addViewController("/").setViewName("/index");
        super.addViewControllers(registry);
    }


    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/images/kaptcha.jpg");
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

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                SimpleModule module = new SimpleModule();
                module.addDeserializer(String.class, new StdDeserializer<String>(String.class) {

                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        String result = StringDeserializer.instance.deserialize(p, ctxt);
                        return StringUtils.trimToNull(result);
                    }
                });
                objectMapper.registerModule(module);

            }
        }
    }


    //    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper){
//
//
////        objectMapper.getSerializerProvider().
//
//                //设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
//        //Include.Include.ALWAYS 默认
//        //Include.NON_DEFAULT 属性为默认值不序列化
//        //Include.NON_EMPTY 属性为 空（""）  或者为 NULL 都不序列化
//        //Include.NON_NULL 属性为NULL 不序列化
////        objectMapper.configure(SerializationFeature, false);
//        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//        //设置将MAP转换为JSON时候只转换值不等于NULL的
//        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,false);
////        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
////        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        //设置有属性不能映射成PO时不报错
////        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
//       return new MappingJackson2HttpMessageConverter();
//    }
    @Bean
    public ObjectMapper jsonMapper() {


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //设置有属性不能映射成PO时不报错
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

//    public static class MyEmptyStringJsonSerializer extends JsonSerializer<Object>{
//        @Override
//        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException{
//            if (value != null && value instanceof String && StringUtils.trimToNull(value.toString()) == null) {
//                jgen.writeNull();
//            } else {
//                jgen.writeObject(value);
//            }
//        }
//    }
//
//
//    public class MyBeanSerializerModifier extends BeanSerializerModifier {
//
//        private JsonSerializer<Object> myEmptyStringJsonSerializer = new MyEmptyStringJsonSerializer();
//
//        @Override
//        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
//                                                         List<BeanPropertyWriter> beanProperties) {
//            // 循环所有的beanPropertyWriter
//            for (int i = 0; i < beanProperties.size(); i++) {
//                BeanPropertyWriter writer = beanProperties.get(i);
//                // 判断字段的类型，如果是array，list，set则注册nullSerializer
//                if (isArrayType(writer)) {
//                    //给writer注册一个自己的nullSerializer
//                    writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
//                }
//            }
//            return beanProperties;
//        }
//
//        // 判断是什么类型
//        protected boolean isArrayType(BeanPropertyWriter writer) {
//            Class<?> clazz = writer.getPropertyType();
//            return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
//
//        }
//
//        protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
//            return _nullArrayJsonSerializer;
//        }
//    }


}
