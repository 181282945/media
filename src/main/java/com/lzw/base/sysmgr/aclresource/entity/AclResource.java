package com.lzw.base.sysmgr.aclresource.entity;

import com.lzw.common.dto.param.ParamDto;
import com.lzw.core.entity.annotation.BaseEntityMapper;
import com.lzw.core.entity.BaseEntity;
import com.lzw.core.entity.annotation.IsNotNull;
import com.lzw.core.entity.annotation.Transient;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclResource")
@BaseEntityMapper(tableName = "acl_resource")
public class AclResource extends BaseEntity implements Comparable<AclResource> {

    public AclResource() {
    }

    public AclResource(Integer id,String code, String name, String path, String type,Boolean isMenu) {
        this.setId(id);
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
        this.isMenu = isMenu;
    }

    /**
     * 模块资源造方法
     *
     * @param code
     * @param name
     * @param path
     * @param type
     * @param homePage
     */
    public AclResource(Integer id,String code, String name, String path, String type, String homePage,Boolean isMenu) {
        this.setId(id);
        this.code = code;
        this.name = name;
        this.path = path;
        this.type = type;
        this.homePage = homePage;
        this.isMenu = isMenu;
    }

//    /**
//     * 重写equals 为了可以当做key使用
//     *
//     * @param o
//     * @return
//     */
//    public boolean equals(Object o) {
//        if (o == this) return true;
//        if (!(o instanceof AclResource))
//            return false;
//        AclResource x = (AclResource) o;
//        if (x.identify.equals(this.identify))
//            return true;
//        return false;
//    }
//
//    public int hashCode() {
//        return Objects.hash(identify);
//    }

    //资源编码
    @IsNotNull
    private String code;
    /**
     * 资源类型,可以是模块
     * 或者是特殊的精细控制
     */
    @IsNotNull
    private String type;
    //请求地址
    @IsNotNull
    private String path;

    /**
     * 模块主页
     */
    private String homePage;
    //序号
    private Integer seq;
    //资源名称
    @IsNotNull
    private String name;
    //模块ID
    private Integer moduleId;
    //菜单ID(属于哪个菜单的模块)
    private Integer menuId;



    //瞬时,是否精确权限控制
    @Transient
    private Boolean isAuth;

    /**
     * 是否显示于菜单项
     */
    private Boolean isMenu;

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

    public Boolean isAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public Boolean getMenu() {
        return isMenu;
    }

    public void setMenu(Boolean menu) {
        isMenu = menu;
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


    //---------------------------------------------------------枚举--------------

    /**
     * Created by 为 on 2017-4-8.
     */
    public enum Type {
        MODULE("module","模块"),METHOD("method","方法");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        Type(String code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code){
            for(Type item : Type.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] typeParams = new ParamDto[Type.values().length];

            for(int i=0;i<typeParams.length;i++){
                typeParams[i] = new ParamDto(Type.values()[i].getCode(),Type.values()[i].getName());
            }
            return  typeParams;
        }



        //-----------------------------------getter and setter---------------------------------------------------------


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
    }


}
