package com.sunjung.common.dto.jqgrid;

/**
 * Created by 为 on 2017-4-17.
 */
public class JqgridFormat {

    public JqgridFormat(){}

    public JqgridFormat(String displayName,String name,String index,boolean editable,boolean search,boolean hidden){
        this.displayName = displayName;
        this.name = name;
        this.index = index;
        this.editable = editable;
        this.search = search;
        this.hidden = hidden;
    }


    private String displayName;
    private String name;
    private String index;
    private String width;
    private boolean editable;
    private boolean search;
    private boolean hidden;



    //-----------------------------------getter and setter---------------------


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
