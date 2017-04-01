package com.sunjung.test.service.impl;

import com.sunjung.core.service.BaseServiceImpl;
import com.sunjung.test.dao.TestEntityMapper;
import com.sunjung.test.entity.TestEntity;
import com.sunjung.test.service.TestEntityService;
import org.springframework.stereotype.Service;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
@Service("testEntityService")
public class TestEntityServiceImpl extends BaseServiceImpl<TestEntity,TestEntityMapper> implements TestEntityService {
}
