package com.aisino.base.invoice.userinfo.service;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.userinfo.dao.UserInfoMapper;
import com.aisino.base.invoice.userinfo.dto.EditPasswordDto;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
public interface UserInfoService extends BaseService<UserInfo,UserInfoMapper> {

    UserInfo getUserByUsrno(String usrno);

    /**
     *  分页查询
     */
    List<UserInfo> findPageUserInfo(UserInfo userInfo, PageAndSort pageAndSort);

    /**
     *  先检验修改密码的规则,然后再保存
     */
    ResultDataDto editPwdByCurrentUser(EditPasswordDto editPasswordDto, String verificationCode, HttpServletRequest request);

    /**
     *  用户注册,检验规则,通过保存
     */
    void regByUser(UserInfo userInfo, String repeatPassword);

    /**
     * 填充用户的角色ID
     * @param userInfos
     */
    void fillRoleId(List<UserInfo> userInfos);

    /**
     *  后台用户新增新用户
     */
    void addByAclUser(UserInfo userInfo, String repeatPassword, EnInfo enInfo);
}
