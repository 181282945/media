package com.aisino.common.model.xml.util;

import com.aisino.common.model.xml.BaseXmlModel;
import com.aisino.common.util.HttpRequestor;
import com.aisino.core.listener.MarshallerListener;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 为 on 2017-5-10.
 */
public class XmlModelUtil {

    /**
     * 对象转XML
     * @param entity
     * @param clz
     * @param <T>
     * @return
     */
    public static <T extends BaseXmlModel> String beanToXmlStr(T entity, Class<T> clz) {
        JAXBContext jc;
        Writer writer = null;
        try {
            jc = JAXBContext.newInstance(clz);
            Marshaller ms = jc.createMarshaller();
            writer = new StringWriter();
            ms.setListener(MarshallerListener.instance);
            ms.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
            ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
            ms.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xm头声明信息
            ms.marshal(entity, writer);
            String xmlStr = writer.toString();
            return xmlStr;
        } catch (JAXBException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * XML转对象
     * @param info
     * @param clz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T extends BaseXmlModel> T xmlStrToBean(String info, Class<T> clz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(info));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T extends BaseXmlModel> String doPost(T entity, Class<T> clz, String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("xml", beanToXmlStr(entity, clz));
        try {
            return new HttpRequestor().doPost(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
