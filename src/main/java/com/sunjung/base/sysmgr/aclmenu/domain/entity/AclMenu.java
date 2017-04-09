package com.sunjung.base.sysmgr.aclmenu.domain.entity;

import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclMenu")
@BaseEntityMapper(tableName = "acl_menu")
public class AclMenu extends BaseEntity {

    public AclMenu(){}

    public AclMenu(String code,String name,Integer seq){
        this.code = code;
        this.name = name;
        this.seq = seq;
    }

    /**
     * 重写equals 为了可以当做key使用
     * @param o
     * @return
     */
    public boolean equals(Object o){
        AclMenu x = (AclMenu)o;
        if(x.code.equals(this.code))
            return true;
        return false;
    }

    public int hashCode(){
        return code.hashCode();
    }

    /**
     * 菜单编码
     */
    private String code;
    /**
     * 菜单名称(显示名称)
     */
    private String name;

    /**
     * 序号(菜单显示序号)
     */
    private Integer seq;

    //-----------------------------------getter and setter----------------------------------------

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
