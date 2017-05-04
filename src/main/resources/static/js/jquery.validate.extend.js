//用户名
$.validator.addMethod("username", function(value, element) {
    var regUsername = /^[\da-zA-Z_]+$/;
    return this.optional(element) || (regUsername.test( value ) );
}, "不符合规则!");
//密码 [1-9][a-z][_]至少两种组合
$.validator.addMethod("password", function(value, element) {
    var regPassword = /^(?![^a-zA-Z]+$)(?!\D+$)/;
    return this.optional(element) || (regPassword.test( value ) );
}, "不符合规则!");
//手机号码
$.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "手机号码格式错误");

//固话
$.validator.addMethod("isTel", function(value, element) {
    var tel = /^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/; //电话号码格式010-12345678
    return this.optional(element) || (tel.test(value));
}, "请正确填写电话号码");