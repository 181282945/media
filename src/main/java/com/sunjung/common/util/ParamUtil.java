package com.sunjung.common.util;

import com.sunjung.common.dto.param.ParamDto;
import com.sunjung.core.util.Delimiter;

/**
 * Created by ZhenWeiLai on 2017/4/17.
 */
public class ParamUtil {

    public static String JqgridCheckBoxVal(String var1,String var2){
        return var1 + Delimiter.COLON + var2;
    }

    public static String JqgridSelectVal(ParamDto... paramDtos){
        String value = "";
        for(int i=0;i<paramDtos.length;i++){
            value += paramDtos[i].getValue() + Delimiter.COLON.getDelimiter() + paramDtos[i].getName() ;
            if(i<paramDtos.length-1)
                value += Delimiter.SEMICOLON.getDelimiter();
        }

        return value;
    }



}
