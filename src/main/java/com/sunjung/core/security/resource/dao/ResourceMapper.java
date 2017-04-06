package com.sunjung.core.security.resource.dao;

import com.sunjung.core.dao.BaseMapper;
import com.sunjung.core.security.resource.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {
}
