$(document).ajaxError(function (e, xhr, opt) {
    if (xhr.status == 401) {
        location.href = $.parseJSON(xhr.responseText).datas;
    } else {
        layer.alert($.parseJSON(xhr.responseText).message,{
            icon: 2
        });
    }
});


/**
 * 去到首页
 */
function toIndex() {
    window.location.href = "/index"
}

/**
 * 请求完成如果操作失败,提示信息
 * @param responseText
 */
function afterCompleteMsg(data) {
    var res = $.parseJSON(data.responseText);
    if (res.operateCode == "S")
        jQuery("#grid-table").trigger("reloadGrid");

    if (res.operateCode == "F")
        layer.alert(res.message,{
            icon: 2
        });
}


function commonCompleteMsg(data, gridTable) {
    if (data.operateCode == "S")
        jQuery("#" + gridTable).trigger("reloadGrid");

    if (data.operateCode == "F")
        layer.alert(data.message,{
            icon: 2
        });
}

function commonCompleteMsg(data) {
    if (data.operateCode == "F")
        layer.alert(data.message,{
            icon: 2
        });
}


/**
 *
 * @param message
 */
function layerAlertMsg(message) {
    layer.alert(message);
}
//图标为绿色钩子
function layerAlertRight(message) {
    layer.alert(message,{icon: 1});
}
//图标为红叉
function layerAlertWrong(message) {
    layer.alert(message,{icon: 2});
}
//图标为黄色问号
function layerAlertQuestion(message) {
    layer.alert(message,{icon: 3});
}

//图标为灰色锁
function layerAlertLock(message) {
    layer.alert(message,{icon: 4});
}

//图标为红色不开心的脸
function layerAlertSad(message) {
    layer.alert(message,{icon: 5});
}

//图标为绿色开心的脸
function layerAlertHappy(message) {
    layer.alert(message,{icon: 6});
}

//图标为黄色感叹号
function layerAlertExclamation(message) {
    layer.alert(message,{icon: 7});
}


function isBlank(obj) {
    if (obj == null || obj == 'undefined' || obj == undefined || jQuery.trim(obj) == '' ) {
        return true;
    }
    return false;
}


/**
 * 自定义查询按钮
 */
$('#btnQuery').click(function () {
    var formData = $('#queryForm').serializeJSON();
    var postData = $("#grid-table").jqGrid("getGridParam", "postData");
    $.extend(postData, formData);
    $("#grid-table").jqGrid('setGridParam', {search: true, page: 1}).trigger("reloadGrid");
});
