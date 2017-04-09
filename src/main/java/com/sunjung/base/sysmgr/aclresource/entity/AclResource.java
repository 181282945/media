package com.sunjung.base.sysmgr.aclresource.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclResource")
@BaseEntityMapper(tableName = "acl_resource")
public class AclResource extends BaseEntity {


    public AclResource(){}

    public AclResource(String code,String name,String path,String type){
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public AclResource(String code,String name,String path,String type,String homePage){
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
        this.homePage = homePage;
    }

    /**
     * 重写equals 为了可以当做key使用
     * @param o
     * @return
     */
    public boolean equals(Object o){
        AclResource x = (AclResource)o;
        if(x.path.equals(this.path))
            return true;
        return false;
    }

    public int hashCode(){
        return path.hashCode();

    }

    //资源编码
    private String code;
    /**
     * 资源类型,可以是模块
     * 或者是特殊的精细控制
     */
    private String type;
    //请求地址
    private String path;

    /**
     * 模块主页
     */
    private String homePage;
    //序号
    private Integer seq;
    //资源名称
    private String name;
    //模块ID
    private Integer moduleId;
    //菜单ID(属于哪个菜单的模块)
    private Integer menuId;


    //-------------------------------------getter and setter-----------------------------------------------

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }


    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
}
