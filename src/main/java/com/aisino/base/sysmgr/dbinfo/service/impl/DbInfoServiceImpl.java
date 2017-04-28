package com.aisino.base.sysmgr.dbinfo.service.impl;

import com.aisino.base.sysmgr.dbinfo.dao.DbInfoMapper;
import com.aisino.base.sysmgr.dbinfo.entity.DbInfo;
import com.aisino.base.sysmgr.dbinfo.service.DbInfoService;
import com.aisino.common.params.SystemParameter;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 为 on 2017-4-27.
 */
@Service("dbInfoService")
public class DbInfoServiceImpl extends BaseServiceImpl<DbInfo,DbInfoMapper> implements DbInfoService {

    @Autowired
    private SystemParameter systemParameter;

    private static final String mysqlDriver = "com.mysql.jdbc.Driver";



    @Override
    public Integer addByTaxNo(String taxNo){
       DbInfo dbInfo = this.createDB(taxNo);
       return this.addEntity(dbInfo);
    }

    @Override
    public DbInfo getDbInfoByTaxNo(String taxNo){
        Specification<DbInfo> specification = new Specification<>(DbInfo.class );
        specification.addQueryLike(new QueryLike("taxNo", QueryLike.LikeMode.Like.Eq,taxNo));
        return this.getOne(specification);
    }




    /**
     * 默认数据库
     */
    private static final String defaultDb = "mysql";

    private DbInfo createDB(String taxno) {
        DbInfo dbInfo = new DbInfo();
        dbInfo.setTaxNo(taxno);
//        jdbc:mysql://localhost:3306/
        dbInfo.setDbname(systemParameter.getDbNamePrefix() + taxno);
        dbInfo.setDbaddr(systemParameter.getDbaddr());
        dbInfo.setDbport(systemParameter.getDbport());
        dbInfo.setDbusr(systemParameter.getUsername());
        dbInfo.setDbpwd(systemParameter.getPassword());
        String url = "jdbc:mysql://" + dbInfo.getDbaddr() + ":" + dbInfo.getDbport() + "/";
//        String username = dbInfo.getDbusr();
//        String password = dbInfo.getPassword();

        String orderinfoSql = systemParameter.getOrderinfoSql();

        Connection conn = null;
        Connection newConn = null;

        String dbname =  systemParameter.getDbNamePrefix() + taxno;

        try {
            Class.forName(mysqlDriver);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
//            String tableSql = "create table t_user (username varchar(50) not null primary key,"
//                    + "password varchar(20) not null ); ";
            String databaseSql = "create database " + dbname;

            conn = DriverManager.getConnection(url + defaultDb, dbInfo.getDbusr(), dbInfo.getDbpwd());
            Statement smt = conn.createStatement();
            if (conn != null) {
                System.out.println("数据库连接成功!");

                smt.executeUpdate(databaseSql);

                newConn = DriverManager.getConnection(url + dbname,
                        dbInfo.getDbusr(), dbInfo.getDbpwd());
                if (newConn != null) {
                    System.out.println("已经连接到新创建的数据库：" + dbname);

                    Statement newSmt = newConn.createStatement();
                    int i = newSmt.executeUpdate(orderinfoSql);//DDL语句返回值为0;
                    if (i == 0) {
                        System.out.println("orderinfoSql 表已经创建成功!");
                    }

                    return dbInfo;
                }
            }

        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }finally {
            try {
                conn.close();
                newConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



}
