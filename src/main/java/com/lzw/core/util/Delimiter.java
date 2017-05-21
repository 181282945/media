package com.lzw.core.util;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public enum Delimiter {
    //逗号
    COMMA(","),
    //分号
    SEMICOLON(";"),
    //中划线;
    INTHEBARS("-"),
    //下划线
    UNDERLINE("_"),
    //星号
    ASTERISK("*"),
    //点号
    POINT("."),
    //冒号(英文)
    COLON(":");

    /**
     * 分割符号
     */
    final private String delimiter;

    Delimiter(String delimiter){
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
