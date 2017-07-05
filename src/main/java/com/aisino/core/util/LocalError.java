package com.aisino.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-6-20.
 */
public class LocalError {
    /**
     * 错误信息 LIST 类型
     */
    private static ThreadLocal<List<String>> message = new ThreadLocal<>();

    public static ThreadLocal<List<String>> getMessage() {
        if (message.get() == null) {
            message.set(new ArrayList<String>());
        }
        return message;
    }

    /**
     * 错误信息 MAP 类型
     */
    private static ThreadLocal<Map<String,String>> mapMessage = new ThreadLocal<>();

    public static ThreadLocal<Map<String,String>> getMapMessage() {
        if (mapMessage.get() == null) {
            mapMessage.set(new HashMap<String, String>());
        }
        return mapMessage;
    }
}
