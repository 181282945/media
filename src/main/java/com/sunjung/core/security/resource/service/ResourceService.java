package com.sunjung.core.security.resource.service;

import com.sunjung.core.security.resource.dao.ResourceMapper;
import com.sunjung.core.security.resource.entity.Resource;
import com.sunjung.core.service.BaseService;
import org.springframework.stereotype.Repository;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
public interface ResourceService extends BaseService<Resource,ResourceMapper> {
}
