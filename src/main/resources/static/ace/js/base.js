/**
 * 请求完成如果操作失败,提示信息
 * @param responseText
 */
function afterCompleteMsg(data){
    var res = $.parseJSON(data.responseText);
    if(res.operateCode == "F")
        layer.open({
            content: res.message,
            scrollbar: false
        });
}