package com.lzw.base.sysmgr.aclmenu.domain.entity;

import com.lzw.core.entity.BaseEntity;
import com.lzw.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

import java.util.Objects;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclMenu")
@BaseEntityMapper(tableName = "acl_menu")
public class AclMenu extends BaseEntity implements Comparable<AclMenu> {

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
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof AclMenu))
            return false;
        AclMenu x = (AclMenu)o;
        if(x.code.equals(this.code))
            return true;
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(code);
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

    public AclMenu setCode(String code) {
        this.code = code;
        return this;
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

    @Override
    public int compareTo(AclMenu o) {
        if(this.seq ==null || o.seq == null)
            return -1;
        return this.seq.compareTo(o.seq);
    }
}
