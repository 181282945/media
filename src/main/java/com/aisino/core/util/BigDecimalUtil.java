package com.aisino.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by 为 on 2017-6-23.
 * BigDecimalUtil 工具类
 */
public class BigDecimalUtil {

    /**
     * 除法,四舍五入
     */
    public static String divide(String var1,String var2,int scale){
       return new BigDecimal(var1).divide(new BigDecimal(var2),scale, RoundingMode.HALF_UP).toString();
    }


    /**
     * 乘法,四舍五入
     */
    public static String multiply(String var1,String var2,int scale){
        return new BigDecimal(var1).multiply(new BigDecimal(var2)).setScale(scale,RoundingMode.HALF_UP).toString();
    }


}
