package com.aisino.common.util;

import com.aisino.common.dto.param.ParamDto;
import com.aisino.core.util.Delimiter;

/**
 * Created by ZhenWeiLai on 2017/4/17.
 */
public class ParamUtil {

    public static String JqgridCheckBoxVal(String var1, String var2) {
        return var1 + Delimiter.COLON + var2;
    }

    @Deprecated
    public static String JqgridSelectVal(ParamDto... paramDtos) {
        String value = "" + Delimiter.COLON.getDelimiter() + "全部" + Delimiter.SEMICOLON.getDelimiter();
        for (int i = 0; i < paramDtos.length; i++) {
            value += paramDtos[i].getValue() + Delimiter.COLON.getDelimiter() + paramDtos[i].getName();
            if (i < paramDtos.length - 1)
                value += Delimiter.SEMICOLON.getDelimiter();
        }

        return value;
    }

    public static String JqgridSelectVal(FirstOption firstOption, ParamDto... paramDtos) {
        String value = "" + Delimiter.COLON.getDelimiter() + firstOption.getValue() + Delimiter.SEMICOLON.getDelimiter();
        for (int i = 0; i < paramDtos.length; i++) {
            value += paramDtos[i].getValue() + Delimiter.COLON.getDelimiter() + paramDtos[i].getName();
            if (i < paramDtos.length - 1)
                value += Delimiter.SEMICOLON.getDelimiter();
        }

        return value;
    }


    public enum FirstOption {
        SELECT("全部"), EDIT("");

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
