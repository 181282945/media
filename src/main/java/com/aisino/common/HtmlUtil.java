package com.aisino.common;

import com.aisino.common.util.ParamUtil;
import com.aisino.e9.entity.parameter.pojo.Parameter;

import java.util.List;

/**
 * Created by 为 on 2017-6-12.
 */
public class HtmlUtil {

    /**
     *  根据参数列表生成option HTML 代码
     */
    public static String createOption(ParamUtil.FirstOption firstOption, List<Parameter> parameters) {
        StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
        for (Parameter parameter : parameters) {
            selectString.append("<option role=\"option\" value=\"" + parameter.getCode() + "\">" + parameter.getName() + "</option>");
        }
        return selectString.toString();
    }

    /**
     *  根据参数列表生成option HTML 代码
     */
    public static String createOption(ParamUtil.FirstOption firstOption, Parameter [] parameters) {
        StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
        for (Parameter parameter : parameters) {
            selectString.append("<option role=\"option\" value=\"" + parameter.getCode() + "\">" + parameter.getName() + "</option>");
        }
        return selectString.toString();
    }

}
