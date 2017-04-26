package com.aisino.base.invoice.userinfo.service.impl;

import com.aisino.base.invoice.userinfo.dao.UserInfoMapper;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-24.
 */
@Service("userInfoService")
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo,UserInfoMapper> implements UserInfoService {

    @Override
    public UserInfo getUserByUsrno(String usrno) {
        return getMapper().getUserByUsrno(usrno);
    }


    @Override
    protected void validateAddEntity(UserInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        if(!entity.getPassword().equals(entity.getRepeatPassword())){
            throw new RuntimeException("两次密码不相同!");
        }
    }


}
