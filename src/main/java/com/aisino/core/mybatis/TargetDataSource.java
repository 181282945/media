package com.aisino.core.mybatis;

/**
 * Created by ZhenWeiLai on 2016/11/22.
 * 配置读写分离或者多数据源
 */
public enum TargetDataSource {

    WRITE("write","主库"), READ("read","从库"), INFO("info","库信息");

    final private String code;

    final private String name;

    TargetDataSource(String _code,String _name) {
        this.code = _code;
        this.name = _name;
    }

    public String getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

    public static String getNameByCode(String _code){
        for(TargetDataSource item : TargetDataSource.values()){
            if(item.getCode().equals(_code)){
                return item.getName();
            }
        }
        return "";
    }
}
