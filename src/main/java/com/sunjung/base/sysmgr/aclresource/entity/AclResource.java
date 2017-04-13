package com.sunjung.base.sysmgr.aclresource.entity;

import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
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

    /**
     * 模块资源造方法
     * @param code
     * @param name
     * @param path
     * @param type
     * @param homePage
     * @param identify
     */
    public AclResource(String code,String name,String path,String type,String homePage,Integer identify){
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
        this.homePage = homePage;
        this.identify = identify;
    }

    /**
     * 方法资源构造方法
     * @param code
     * @param name
     * @param path
     * @param type
     * @param identify
     */
    public AclResource(String code,String name,String path,String type,Integer identify){
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
        this.identify = identify;
    }

    /**
     * 重写equals 为了可以当做key使用
     * @param o
     * @return
     */
    public boolean equals(Object o){
        AclResource x = (AclResource)o;
        if(AclResourceType.MODULE.getCode().equals(this.type)){
            if(x.identify.intValue()==this.identify)
                return true;
            return false;
        }else{
            return super.equals(o);
        }
    }

    public int hashCode(){
        if(AclResourceType.MODULE.getCode().equals(this.type)){
            return identify;
        }else{
            return super.hashCode();
        }
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

    /**
     * 唯一标识,因为在反射模块的时候并没有ID
     * 简单类名的hashCode
     */
    private Integer identify;

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

    public Integer getIdentify() {
        return identify;
    }

    public void setIdentify(Integer identify) {
        this.identify = identify;
    }


    /**
     * 返回模块地址
     * @return
     */
    public String getHref(){
        return "javascript:addTabs({id:'" + this.getId() + "',title: '" + this.name + "',close: true,url:'" +this.homePage+ "'});";
    }
}
