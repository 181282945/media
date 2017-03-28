package com.sunjung.common.usersigninfo.service;

import com.sunjung.common.usersigninfo.entity.UserSignInfo;
import com.sunjung.core.service.BaseService;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 */
public interface UserSignInfoService extends BaseService<UserSignInfo> {
    UserSignInfo getUserSignInfoByName(String name);
}
