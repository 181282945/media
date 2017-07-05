package com.aisino.common.util;

/**
 * Created by 为 on 2017-5-25.
 */
public class RegexUtil {
    /**
     * 判断是否合法数字,包括小数点
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }
}
