package com.aisino.base.sysmgr.aclresource.entity;

import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.Transient;
import org.apache.ibatis.type.Alias;

import java.util.Objects;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclResource")
@BaseEntityMapper(tableName = "acl_resource")
public class AclResource extends BaseEntity implements Comparable<AclResource> {

    public AclResource() {
    }

    public AclResource(String code, String name, String path, String type) {
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
    }

    /**
     * 模块资源造方法
     *
     * @param code
     * @param name
     * @param path
     * @param type
     * @param homePage
     * @param identify
     */
    public AclResource(String code, String name, String path, String type, String homePage, String identify) {
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
        this.homePage = homePage;
        this.identify = identify;
    }

    /**
     * 方法资源构造方法
     *
     * @param code
     * @param name
     * @param path
     * @param type
     * @param identify
     */
    public AclResource(String code, String name, String path, String type, String identify) {
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
        this.identify = identify;
    }

    /**
     * 重写equals 为了可以当做key使用
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AclResource))
            return false;
        AclResource x = (AclResource) o;
        if (x.identify.equals(this.identify))
            return true;
        return false;
    }

    public int hashCode() {
        return Objects.hash(identify);
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



    //瞬时,是否精确权限控制
    @Transient
    private Boolean isAuth;



    /**
     * 唯一标识,因为在反射模块的时候并没有ID
     * 简单类名的hashCode
     */
    private String identify;

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

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }


    public Boolean isAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    /**
     * 返回模块地址
     *
     * @return
     */
    public String getHref() {
        return "javascript:addTabs({id:'" + this.getId() + "',title: '" + this.name + "',close: true,url:'" + this.homePage + "'});";
    }

    @Override
    public int compareTo(AclResource o) {
        if (this.seq == null || o.seq == null)
            return -1;
        return this.seq.compareTo(o.seq);
    }
}
