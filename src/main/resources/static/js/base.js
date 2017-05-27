$(document).ajaxError(function(e,xhr,opt){
    layer.alert($.parseJSON(xhr.responseText).message);
});

/**
 * 请求完成如果操作失败,提示信息
 * @param responseText
 */
function afterCompleteMsg(data){
    var res = $.parseJSON(data.responseText);
    if(res.operateCode == "S")
        jQuery("#grid-table").trigger("reloadGrid");

    if(res.operateCode == "F")
        layer.open({
            content: res.message,
            scrollbar: false
        });
}


function commonCompleteMsg(data,gridTable){
    if(data.operateCode == "S")
        jQuery("#"+gridTable).trigger("reloadGrid");

    if(data.operateCode == "F")
        layer.open({
            content: data.message,
            scrollbar: false
        });
}

function commonCompleteMsg(data){
    if(data.operateCode == "F")
        layer.open({
            content: data.message,
            scrollbar: false
        });
}


function layerAlertMsg(message){
        layer.open({
            content: message,
            scrollbar: false
        });
}








