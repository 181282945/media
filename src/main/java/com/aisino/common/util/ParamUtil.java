package com.aisino.common.util;

import com.aisino.core.util.Delimiter;
import com.aisino.e9.entity.parameter.pojo.Parameter;

/**
 * Created by ZhenWeiLai on 2017/4/17.
 */
public class ParamUtil {

    public static String JqgridCheckBoxVal(String var1, String var2) {
        return var1 + Delimiter.COLON + var2;
    }

    @Deprecated
    public static String JqgridSelectVal(Parameter... parameters) {
        String value = "" + Delimiter.COLON.getDelimiter() + "全部" + Delimiter.SEMICOLON.getDelimiter();
        for (int i = 0; i < parameters.length; i++) {
            value += parameters[i].getCode() + Delimiter.COLON.getDelimiter() + parameters[i].getName();
            if (i < parameters.length - 1)
                value += Delimiter.SEMICOLON.getDelimiter();
        }

        return value;
    }

    public static String JqgridSelectVal(FirstOption firstOption, Parameter... parameters) {
        String value = "" + Delimiter.COLON.getDelimiter() + firstOption.getValue() + Delimiter.SEMICOLON.getDelimiter();
        for (int i = 0; i < parameters.length; i++) {
            value += parameters[i].getCode() + Delimiter.COLON.getDelimiter() + parameters[i].getName();
            if (i < parameters.length - 1)
                value += Delimiter.SEMICOLON.getDelimiter();
        }

        return value;
    }


    public enum FirstOption {
        VIEW(""),QUERY("全部"), SELECT("请选择"), EDIT("请选择");

        private String value;


        //构造方法
        FirstOption(String value) {
            this.value = value;
            this.value = value;
        }

        //-----------------------------------getter and setter---------------------------------------------------------


        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
