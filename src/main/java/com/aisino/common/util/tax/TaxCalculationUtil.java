package com.aisino.common.util.tax;

import java.math.BigDecimal;

/**
 * Created by 为 on 2017-5-11.
 * 计算税工具类
 */
public class TaxCalculationUtil {


    /**
     *  根据税率,含税金额,计算不含税金额
     */
    public static String calcBHSJE(String taxRateTem,String hsje){
        BigDecimal taxRate = new BigDecimal(taxRateTem);
        BigDecimal xmje = new BigDecimal(hsje);
        //计算不含税金额
        BigDecimal bhsje =  xmje.divide(taxRate.divide(new BigDecimal(100)).add(new BigDecimal(1)),2, BigDecimal.ROUND_HALF_UP);
        return  bhsje.toString();
    }


    /**
     *  相加返回负数
     */
    public static String merge(String value1,String value2){
        BigDecimal result = new BigDecimal(value1).add(new BigDecimal(value2));
        return  negative(result.toString());
    }

    /**
     *  计算税额
     */
    public static String calcSE(String taxRateTem,String xmjeTem){
        //计算不含税金额
        BigDecimal bhsje =  new BigDecimal(calcBHSJE(taxRateTem,xmjeTem));
        //计算税额
        BigDecimal se = new BigDecimal(xmjeTem).subtract(bhsje);
        se = se.setScale(2, BigDecimal.ROUND_HALF_UP);
        return se.toString();
    }

    /**
     * 根据项目单价,数量计算项目金额,保留两位小数,四舍五入
     * @return
     */
    public static String calcItemPrice(String unitPrice,String qty){
        BigDecimal itemPrice = new BigDecimal(unitPrice).multiply(new BigDecimal(qty).abs());
        return scaleII(itemPrice.toString());
    }


    /**
     *  保留八位小数,四舍五入
     */
    public static String scaleVIII(String number){
        BigDecimal temp = new BigDecimal(number);
        temp = temp.setScale(8, BigDecimal.ROUND_HALF_UP);
        return temp.toString();
    }

    /**
     *  保留两位小数,四舍五入
     */
    public static String scaleII(String value){
        BigDecimal temp = new BigDecimal(value);
        temp = temp.setScale(2, BigDecimal.ROUND_HALF_UP);
        return temp.toString();
    }

    /**
     *  绝对返回负数
     */
    public static String negative(String value){
        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.abs().multiply(new BigDecimal(-1));
        return decimal.toString();
    }



    /**
     *  加法运算
     */
    public static String add(String value1,String value2){
        BigDecimal result = new BigDecimal(value1).add(new BigDecimal(value2));
        return  result.toString();
    }

}
