/**
 * 模块切换按钮
 * @param cellvalue
 * @param options
 * @param rowObject
 * @returns {string}
 */
function moduleSwitch(cellvalue, options, rowObject) {
    var gridId = options.colModel.formatoptions.subgridTableId;
    if(rowObject.auth == false)
        return '<input onclick="onClickModuleHander(this)" type="checkbox" value="1" gridId="'+gridId+'" offval="0" id='+"'+options.rowId+'"+'"_stock" name="stock" userId="' + options.colModel.formatoptions.value + '" rowid="'+options.rowId+'" role="checkbox" class="editable inline-edit-cell ace ace-switch ace-switch-5"><span class="lbl"></span>';

    return '<input onclick="onClickModuleHander(this)" checked type="checkbox" value="1" gridId="'+gridId+'" offval="0" id=' + "'+options.rowId+'" + '"_stock" name="stock" userId="'+ options.colModel.formatoptions.value +'" rowid="' + options.rowId + '" role="checkbox" class="editable inline-edit-cell ace ace-switch ace-switch-5"><span class="lbl"></span>';
}

/**
 * 模块切换事件处理
 * @param obj
 * @param gridId
 */
function onClickModuleHander(obj,gridId) {
    if (obj.checked) {
        // $(obj).attr("disabled",true);
        $.post("/base/sysmgr/aclrescuser/addByRescIdUserId","rescId=" +  $(obj).attr("rowid") + "&userId=" +  $(obj).attr("userId"),
            function(data){
                commonCompleteMsg(data);
                jQuery("#"+$(obj).attr("gridId")).trigger("reloadGrid");
                // $(obj).removeAttr("disabled");
            }, "json");
    } else {
        // $(obj).attr("disabled",true);
        $.post("/base/sysmgr/aclrescuser/deleteByRescIdUserId","rescId=" +  $(obj).attr("rowid") + "&userId=" +  $(obj).attr("userId"),
            function(data){
                commonCompleteMsg(data);
                jQuery("#"+$(obj).attr("gridId")).trigger("reloadGrid");
                // $(obj).removeAttr("disabled");
            }, "json");
    }
}

/**
 * 方法切换按钮
 * @param cellvalue
 * @param options
 * @param rowObject
 * @returns {string}
 */
function methodSwitch(cellvalue, options, rowObject) {
    var gridId = options.colModel.formatoptions.subgridTableId;
    if(rowObject.auth == false)
        return '<input onclick="onClickMetodHander(this)" type="checkbox" value="1" gridId="'+gridId+'" offval="0" id='+"'+options.rowId+'"+'"_stock" name="stock" userId="' + options.colModel.formatoptions.value + '" rowid="'+options.rowId+'" role="checkbox" class="editable inline-edit-cell ace ace-switch ace-switch-5"><span class="lbl"></span>';

    return '<input onclick="onClickMetodHander(this)" checked type="checkbox" value="1" gridId="'+gridId+'" offval="0" id=' + "'+options.rowId+'" + '"_stock" name="stock" userId="'+ options.colModel.formatoptions.value +'" rowid="' + options.rowId + '" role="checkbox" class="editable inline-edit-cell ace ace-switch ace-switch-5"><span class="lbl"></span>';
}

/**
 * 方法切换事件处理
 * @param obj
 * @param gridId
 */
function onClickMetodHander(obj,gridId) {
    if (obj.checked) {
        // $(obj).attr("disabled",true);
        $.post("/base/sysmgr/acluserauth/addByRescIdUserId","rescId=" +  $(obj).attr("rowid") + "&userId=" +  $(obj).attr("userId"),
            function(data){
                commonCompleteMsg(data);
                jQuery("#"+$(obj).attr("gridId")).trigger("reloadGrid");
                // $(obj).removeAttr("disabled");
            }, "json");
    } else {
        // $(obj).attr("disabled",true);
        $.post("/base/sysmgr/acluserauth/deleteByRescIdUserId","rescId=" +  $(obj).attr("rowid") + "&userId=" +  $(obj).attr("userId"),
            function(data){
                commonCompleteMsg(data);
                jQuery("#"+$(obj).attr("gridId")).trigger("reloadGrid");
                // $(obj).removeAttr("disabled");
            }, "json");
    }
}