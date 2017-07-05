//需要控住能否编辑的元素名称
var displayElement = ['itemNum', 'classifierName', 'specMode', 'itemUnit', 'itemUnit', 'taxRate', 'itemPrice', 'amount'];


/**
 * 获取ObjId
 * @param obj
 * @returns {*|jQuery}
 */
function getObjId(obj) {
    return $(obj).parent("div").parent("div").parent("div").parent("div").attr('objId');
}

/**
 * 获取ObjId
 * @param obj
 * @returns {*|jQuery}
 */
function getRow(obj) {
    return $(obj).parent("div").parent("div").parent("div").parent("div");
}

/**
 * 计算总额
 */
function calcAmount(obj) {

    if (isBlank($(obj).val()))
        return;

    var rowFlag = getObjId(obj);
    //查询本行的单价
    var itemPrice = $("div[objId='" + rowFlag + "']").children().find("[name='itemPrice']").val();
    //查询本行数量
    var itemNum = $("div[objId='" + rowFlag + "']").children().find("[name='itemNum']").val();

    var amount = $("div[objId='" + rowFlag + "']").children().find("[name='amount']").val();

    //如果总价跟数量为空,那么什么都不干
    if (isBlank(itemNum) && isBlank(amount))
        return;
    //如果总价跟单价为空,那么什么也不干
    if (isBlank(itemPrice) && isBlank(amount))
        return;


    // 如果价格为空,总价不为空,数量不为空,那么计算价格
    if ((isBlank(itemPrice) && !isBlank(itemNum) && !isBlank(amount))) {
        itemPrice = numDiv(amount, itemNum).toFixed(6);
        $("div[objId='" + rowFlag + "']").children().find("[name='itemPrice']").val(itemPrice);
        return;
    }

    // 如果数量为空,总价不为空,单价不为空,那么计算数量
    if ((!isBlank(itemPrice) && isBlank(itemNum) && !isBlank(amount))) {
        itemNum = numDiv(amount, itemPrice);
        $("div[objId='" + rowFlag + "']").children().find("[name='itemNum']").val(itemNum);
        return;
    }

    //
    if ((!isBlank(itemPrice) && !isBlank(itemNum))) {
        amount = numMulti(itemPrice, itemNum).toFixed(6);
        //设置金额
        $("div[objId='" + rowFlag + "']").children().find("[name='amount']").val(amount);
    }


}

/**
 * 触发条件
 * 数量,单价
 * 失去焦点时
 * 如果数量,单价不为空计算金额
 * 计算金额,顺带计算税额
 */
function calcAmountAndTax(obj) {
    calcAmount(obj);
    calcTax(obj);
}

/**
 * 当总价发生改变时,或者下拉框改变时
 * 计算税额
 * @param rowFlag
 */
function calcTax(obj) {

    var rowFlag = getObjId(obj);

    //先验证一次是否非法金额,如果是非法金额,强行根据数量以及单价计算一次更正
    calcAmount(obj);

    //如果税率不为空,计算税额
    var taxRate = $("div[objId='" + rowFlag + "']").children().find("[name='taxRate']").val();
    if (isBlank(taxRate))
        return;

    var amount = $("div[objId='" + rowFlag + "']").children().find("[name='amount']").val();

    if (isBlank(amount))
        amount = 0;
    //设置税额
    $("div[objId='" + rowFlag + "']").children().find("[name='tax']").val(numMulti(amount, taxRate));

    calcTotal();
}

/**
 * 计算价税合计
 */
function calcTotal() {
    var rows = $('#tableContent').find('div.prod');
    var totalAmount = 0;
    var totalTax = 0;
    $.each(rows, function (k, v) {
        var amount = $(v).children().find("[name='amount']").val();
        var tax = $(v).children().find("[name='tax']").val();
        if (!isBlank(amount) && !isBlank(tax)) {
            totalAmount = numAdd(amount, totalAmount).toFixed(6);
            totalTax = numAdd(tax, totalTax).toFixed(6);
        }
    });

    totalAmount = new Number(totalAmount).toFixed(2);
    totalTax = new Number(totalTax).toFixed(2);
    $('#totalAmount').html(totalAmount);
    $("input[name='totalAmount']").val(totalAmount);
    $('#totalTax').html(totalTax);
    $("input[name='totalTax']").val(totalTax);

    var totalAmountTax = numAdd(totalTax, totalAmount);

    totalAmountTax = new Number(totalAmountTax).toFixed(2);
    $('#totalAmountTax').html(totalAmountTax);
    $("input[name='totalAmountTax']").val(totalAmountTax);

    $('#totalAmountTaxCN').html(convertCurrency(totalAmountTax));
    $("input[name='totalAmountTaxCN']").val(convertCurrency(totalAmountTax));
}

/**
 * 提交前检查数据合法性
 */
function checkBeforeSubmit() {
    var rows = $('#tableContent').find('div.prod');
    var rowIndex = new Array();
    var rowObj = new Array();


    var buyerName = $('#manualForm').find('input[name="buyerName"]');
    var buyerTel = $('#manualForm').find('input[name="buyerTel"]');
    var sellerName = $('#manualForm').find('input[name="sellerName"]');
    var sellerAddr = $('#manualForm').find('input[name="sellerAddr"]');
    var sellerTel = $('#manualForm').find('input[name="sellerTel"]');
    var totalAmountTax = $('#manualForm').find('input[name="totalAmountTax"]');
    var drawer = $('#manualForm').find('input[name="drawer"]');

    if (isBlank($(buyerName).val())) {
        layer.alert("请填写购货方名称!");
        return;
    }

    if (isBlank($(buyerTel).val())) {
        layer.alert("请填写购货方手机号码!");
        return;
    }

    if (isBlank($(sellerName).val())) {
        layer.alert("请填写销货方名称!");
        return;
    }

    if (isBlank($(sellerAddr).val())) {
        layer.alert("请填写销货方地址!");
        return;
    }

    if (isBlank($(sellerTel).val())) {
        layer.alert("请填写销货方电话!");
        return;
    }

    if (isBlank($(totalAmountTax).val())) {
        layer.alert("缺少价税合计!");
        return;
    }

    if (isBlank($(drawer).val())) {
        layer.alert("开票人不能为空!");
        return;
    }


    $.each(rows, function (k, v) {
        var amount = $(v).children().find("[name='amount']").val();
        var taxRate = $(v).children().find("[name='taxRate']").val();
        var classifierName = $(v).children().find("[name='classifierName']").val();
        if (isBlank(amount) || isBlank(taxRate) || isBlank(classifierName)) {
            rowIndex.push(new Number(k) + 1);
            rowObj.push(v);
        }
    });

    if (rowIndex.length > 0) {
        layer.confirm("行: " + rowIndex + " 不完整数据!", {
            title: '提示',
            btn: ['删除并提交', '返回修改'] //按钮
        }, function (index) {
            //删除不完整数据行
            $.each(rowObj, function (k, v) {
                $(v).remove();
            });
            //判断所剩合法行数
            var rows = $('#tableContent').find('div.prod');
            if (rows.length == 0) {
                layer.alert('缺少商品明细行!');
                return;
            }


            manualInvoice = new Object();//手动开票数据
            manualInvoice.manualOrderDetails = new Array();


            var rows = $('#tableContent').find('div.prod');

            manualInvoice.buyerName = $('#manualForm').find("input[name='buyerName']").val();
            manualInvoice.buyerTaxno = $('#manualForm').find("input[name='buyerTaxno']").val();
            manualInvoice.buyerAddr = $('#manualForm').find("input[name='buyerAddr']").val();
            manualInvoice.buyerTel = $('#manualForm').find("input[name='buyerTel']").val();
            manualInvoice.buyerBankAccount = $('#manualForm').find("input[name='buyerBankAccount']").val();
            manualInvoice.totalAmount = $('#manualForm').find("input[name='totalAmount']").val();
            manualInvoice.totalTax = $('#manualForm').find("input[name='totalTax']").val();
            manualInvoice.totalAmountTax = $('#manualForm').find("input[name='totalAmountTax']").val();
            manualInvoice.sellerName = $('#manualForm').find("input[name='sellerName']").val();
            manualInvoice.sellerTaxno = $('#manualForm').find("input[name='sellerTaxno']").val();
            manualInvoice.sellerAddr = $('#manualForm').find("input[name='sellerAddr']").val();
            manualInvoice.sellerTel = $('#manualForm').find("input[name='sellerTel']").val();
            manualInvoice.sellerBankAccount = $('#manualForm').find("input[name='sellerBankAccount']").val();
            manualInvoice.payee = $('#manualForm').find("input[name='payee']").val();
            manualInvoice.reviewer = $('#manualForm').find("input[name='reviewer']").val();
            manualInvoice.drawer = $('#manualForm').find("input[name='drawer']").val();
            manualInvoice.remarks = $('#manualForm').find("textarea[name='remarks']").val();

            $.each(rows, function (k, v) {
                var manualOrderDetail = new Object();//手动开票订单明细数据
                manualOrderDetail.rowNum = numAdd(k,1);//行数以1开始,所以k+1
                manualOrderDetail.objId = $(v).attr('objId');
                manualOrderDetail.classifierName = $(v).find("input[name='classifierName']").val();
                manualOrderDetail.specMode = $(v).find("input[name='specMode']").val();
                manualOrderDetail.itemUnit = $(v).find("input[name='itemUnit']").val();
                manualOrderDetail.itemNum = $(v).find("input[name='itemNum']").val();
                manualOrderDetail.itemPrice = $(v).find("input[name='itemPrice']").val();
                manualOrderDetail.amount = $(v).find("input[name='amount']").val();
                manualOrderDetail.taxRate = $(v).find("select[name='taxRate']").val();
                manualOrderDetail.tax = $(v).find("input[name='tax']").val();
                manualOrderDetail.discountLineObjId = $(v).find("input[name='discountLineObjId']").val();
                if (!isBlank(manualOrderDetail.discountLineObjId))
                    manualOrderDetail.discountLine = k;//因为行数+1,所以上一行就直接k
                manualInvoice.manualOrderDetails.push(manualOrderDetail);
            });

            $.ajax({
                type: "POST",
                url: '/base/invoice/invoiceinfo/u/manualBilling',
                contentType: "application/json",  //发送信息至服务器时内容编码类型。
                dataType: "json",  // 预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断，比如XML MIME类型就被识别为XML。
                data: JSON.stringify(manualInvoice),
                success: function (json) {
                    layer.alert(json.message);
                    if (json.operateCode == "S") {
                        $('#manualModal').modal('hide');
                        document.getElementById("manualForm").reset();
                        initRows();
                    }
                    layer.close(index);
                }
            });
            layer.close(index);
        }, function (index) {
            layer.close(index);
        });
    }
}


/**
 * 计算折扣率
 *
 */
function calcDiscountRate(obj) {
    // var discountAmount = $(obj).val();
    var discountItemAmount = $('#discountItemAmount').val();
    if (!isBlank($(obj).val())) {

        //如果大于商品金额,那么强制等于商品金额
        if (eval($(obj).val()) > eval(discountItemAmount)) {
            $(obj).val(discountItemAmount);
            $('#discountRate').val(0);
            return;
        }
        //如果小于等于0,那么强制置空
        if (eval($(obj).val()) <= eval(0)) {
            $('#discountRate').val('');
            $(obj).val('');
            return;
        }
        var discountRate = numMulti(numDiv($(obj).val(), discountItemAmount), 100);
        $('#discountRate').val(discountRate);
    } else {
        $('#discountRate').val('');
    }
}

/**
 * 计算折扣金额
 */
function calcDiscountItemAmount(obj) {
    // var discountAmount = $(obj).val();
    var discountItemAmount = $('#discountItemAmount').val();
    if (!isBlank($(obj).val())) {
        //如果折扣率大于100,那么强制改为100,或者折扣率小于0,强行改成0
        if (eval($(obj).val()) >= eval(100)) {
            $(obj).val('');
        }

        if (eval($(obj).val()) < eval(0))
            $(obj).val(0);
        var discountAmount = numMulti(discountItemAmount, numDiv($(obj).val(), 100));
        $('#discountAmount').val(discountAmount);
    } else {
        $('#discountAmount').val('');
    }
}

/**
 *
 * @param arg1
 * @param arg2
 * @returns {number}
 */
function checkClassifierName(obj) {
    var row = getRow(obj);
    if (isBlank($(obj).val())) {
        $(row).find('select[name="taxRate"]').attr('disabled', 'disabled');
        return;
    }
    $(row).find('select[name="taxRate"]').removeAttr('disabled');
}
