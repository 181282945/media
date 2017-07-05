package com.aisino.core.listener;

import javax.xml.bind.Marshaller;
import java.lang.reflect.Field;

/**
 * Created by 为 on 2017-6-14.
 */
public class MarshallerListener extends Marshaller.Listener {

    public static final String BLANK_CHAR = "";

    public static volatile MarshallerListener instance = new MarshallerListener();


    @Override
    public void beforeMarshal(Object source) {
        super.beforeMarshal(source);
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            //获取字段上注解
            try {
                if (f.getType() == String.class && f.get(source) == null) {
                    f.set(source, BLANK_CHAR);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}