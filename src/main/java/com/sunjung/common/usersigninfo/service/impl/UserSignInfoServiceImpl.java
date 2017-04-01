package com.sunjung.common.usersigninfo.service.impl;

import com.sunjung.common.usersigninfo.dao.UserSignInfoMapper;
import com.sunjung.common.usersigninfo.entity.UserSignInfo;
import com.sunjung.common.usersigninfo.service.UserSignInfoService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 */
@Service("userSignInfoService")
public class UserSignInfoServiceImpl extends BaseServiceImpl<UserSignInfo,UserSignInfoMapper> implements UserSignInfoService {

    public UserSignInfo getUserSignInfoByName(String name){
        return getMapper().getUserSignInfoByName(name);
    }

}
