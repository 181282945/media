package com.sunjung.common.dto.jqgrid;

import com.sunjung.core.util.Delimiter;

/**
 * Created by 为 on 2017-4-17.
 * Jqgrid
 */
public class Editable {

    /**
     * 是否可编辑
     */
    private boolean editable;

    /**
     * 可编辑类型
     */
    private EditType editType;

    /**
     * 参数
     */
    private EditOption [] editOptions;

    private String options;

    public Editable(){}

//    public Editable(EditType editType,EditOption... options){
//        if(EditType.CHECKBOX == editType){
//
//        }
//
//    }

    /**
     * 单选框
     * @param var1
     * @param var2
     * @return
     */
    public static Editable checkBox(String var1,String var2){
        Editable editable = new Editable();
        editable.editable = true;
        editable.editType = EditType.CHECKBOX;
        editable.options = "value:\""+ var1 + Delimiter.COLON + var2 +"\"";
        return editable;
    }

    /**
     * 文本框
     * @param size
     * @param maxlength
     * @return
     */
    public static Editable text(String size,String maxlength){
        Editable editable = new Editable();
        editable.editable = true;
        editable.editType = EditType.TEXT;
        editable.options = "size:\""+size+"\",maxlength:\""+maxlength+"\"";
        return editable;
    }

    /**
     * 选择框
     * @param editOptions
     * @return
     */
    public static Editable select(EditOption... editOptions){
        Editable editable = new Editable();
        editable.editable = true;
        editable.editType = EditType.SELECT;
        StringBuilder sb = new StringBuilder("value:\"");
        for(int i=0;i< editOptions.length-1;i++){
            sb.append(editOptions[i].var1 + Delimiter.COLON + editOptions[i].var2 + Delimiter.SEMICOLON);
        }
        sb.append(editOptions[editOptions.length-1].var1+ Delimiter.COLON + editOptions[editOptions.length-1].var2 + Delimiter.SEMICOLON);
        editable.options = sb.toString();
        return editable;
    }

    /**
     * 不可编辑
     * @return
     */
    public static Editable immutable(){
        Editable editable = new Editable();
        editable.editable = false;
        return editable;
    }



    //-------------------------------getter and setter----------------------


    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public EditType getEditType() {
        return editType;
    }

    public void setEditType(EditType editType) {
        this.editType = editType;
    }

    public EditOption[] getEditOptions() {
        return editOptions;
    }

    public void setEditOptions(EditOption[] editOptions) {
        this.editOptions = editOptions;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * Jqgrid可编辑时参数
     */
    static class EditOption{
        private String var1;
        private String var2;

        public static EditOption checkBox(String var1,String var2){
            EditOption editOption = new EditOption();
            editOption.var1 = var1;
            editOption.var2 = var2;
            return editOption;
        }

        public static EditOption text(String size,String maxlength){
            EditOption editOption = new EditOption();
            editOption.var1 = size;
            editOption.var2 = maxlength;
            return editOption;
        }

        //--------------getter and setter-----------------------------------


        public String getVar1() {
            return var1;
        }

        public void setVar1(String var1) {
            this.var1 = var1;
        }

        public String getVar2() {
            return var2;
        }

        public void setVar2(String var2) {
            this.var2 = var2;
        }
    }
}
