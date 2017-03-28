package com.sunjung.test.entity;

import com.sunjung.core.entity.BaseBusinessEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 * 测试实体
 */
@Alias("TestEntity")
@BaseEntityMapper(tableName = "testentity")
public class TestEntity extends BaseBusinessEntity {
    //姓名
    private String name;
    //年龄
    private Integer age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
