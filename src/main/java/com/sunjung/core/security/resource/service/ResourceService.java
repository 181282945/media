package com.sunjung.core.security.resource.service;

import com.sunjung.core.security.resource.dao.ResourceMapper;
import com.sunjung.core.security.resource.entity.Resource;
import com.sunjung.core.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
public interface ResourceService extends BaseService<Resource,ResourceMapper> {
    /**
     * 查询所有模块
     * @return
     */
    List<Resource> findAllModule();

    /**
     * 根据path查询资源是否存在
     * @param path
     * @return
     */
    Resource findByPath(String path);
}
