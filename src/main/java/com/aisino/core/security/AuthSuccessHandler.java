package com.aisino.core.security;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.dbinfo.entity.DbInfo;
import com.aisino.base.sysmgr.dbinfo.service.DbInfoService;
import com.aisino.base.sysmgr.infoschema.schemata.service.SchemataService;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.mybatis.MyRoutingDataSource;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-6.
 * 登录成功处理类
 */
@RestController
public class AuthSuccessHandler{

    @Resource
    private EnInfoService enInfoService;

    @Resource
    private DbInfoService dbInfoService;

    @Resource
    private SchemataService schemataService;

    @Resource
    private MyRoutingDataSource routingDataSource;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
    public ResultDataDto loginSuccess() {
        UserInfo userInfo = SecurityUtil.getCurrentUserInfo();
        /**
         * 加入用户数据源,保存数据在Session
         */
        if (userInfo != null) {
            EnInfo enInfo = enInfoService.getByUsrno(userInfo.getUsrno());
            cuzSessionAttributes.setEnInfo(enInfo);
            if (enInfo != null){
                DbInfo dbInfo = dbInfoService.getDbInfoByTaxNo(enInfo.getTaxno());
                cuzSessionAttributes.setDbInfo(dbInfo);
            }

            if (schemataService.checkDbExist(cuzSessionAttributes.getDbname()))
                cuzSessionAttributes.setDbexist(true);
            else
                cuzSessionAttributes.setDbexist(false);

            cuzSessionAttributes.setUserInfo(userInfo);
            routingDataSource.addCuzDataSource(userInfo);
        }

        return ResultDataDto.addOperationSuccess().setDatas("/index");
    }
}
