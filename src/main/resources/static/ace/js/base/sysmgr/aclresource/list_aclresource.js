function customizeSwitch(cellvalue, options, rowObject) {
    if(rowObject.auth == false)
        return '<input onclick="onClickHander(this)" type="checkbox" value="1" offval="0" id=' + "'+options.rowId+'" + '"_stock" name="stock" rowid="' + options.rowId + '" role="checkbox" class="editable inline-edit-cell ace ace-switch ace-switch-5"><span class="lbl"></span>';

    return '<input onclick="onClickHander(this)" checked type="checkbox" value="1" offval="0" id=' + "'+options.rowId+'" + '"_stock" name="stock" rowid="' + options.rowId + '" role="checkbox" class="editable inline-edit-cell ace ace-switch ace-switch-5"><span class="lbl"></span>';
}


function onClickHander(obj) {
    if (obj.checked) {
        $(obj).attr("disabled",true);
        //询问框
        layer.confirm('开启权限控制,没有此权限的角色以及用户将不能访问!', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.post("/base/sysmgr/aclauth/addByRescId","methodRescId=" +  $(obj).attr("rowid"),
                function(data){
                    commonCompleteMsg(data);
                    layer.closeAll('dialog')
                    $(obj).removeAttr("disabled");
                }, "json");
        }, function(){
            $(obj).removeAttr("disabled");
        });

    } else {
        $(obj).attr("disabled",true);
        //询问框
        layer.confirm('是否确定关闭权限控制?', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.post("/base/sysmgr/aclauth/deleteByRescId","methodRescId=" +  $(obj).attr("rowid"),
                function(data){
                    commonCompleteMsg(data);
                    layer.closeAll('dialog')
                    $(obj).removeAttr("disabled");
                }, "json");
        }, function(){
            $(obj).removeAttr("disabled");
        });
    }
}