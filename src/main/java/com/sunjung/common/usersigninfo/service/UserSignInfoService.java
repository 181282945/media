package com.sunjung.common.usersigninfo.service;

import com.sunjung.common.usersigninfo.dao.UserSignInfoMapper;
import com.sunjung.common.usersigninfo.entity.UserSignInfo;
import com.sunjung.core.service.BaseService;
import org.springframework.stereotype.Repository;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 */
public interface UserSignInfoService extends BaseService<UserSignInfo,UserSignInfoMapper> {
    UserSignInfo getUserSignInfoByName(String name);
}
