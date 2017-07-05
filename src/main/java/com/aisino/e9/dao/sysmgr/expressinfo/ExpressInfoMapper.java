package com.aisino.e9.dao.sysmgr.expressinfo;

import com.aisino.core.dao.BaseMapper;
import com.aisino.e9.entity.expressinfo.pojo.ExpressInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by 为 on 2017-6-12.
 */
@Mapper
public interface ExpressInfoMapper extends BaseMapper<ExpressInfo> {
//    /**
//     * 根据单号查询快递信息
//     */
//    @Select("SELECT * FROM sysmgr_expressinfo WHERE trackingNum = #{trackingNum} LIMIT 1")
//    ExpressInfo getByTrackingNum(@Param("trackingNum")String trackingNum);
}
