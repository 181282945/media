package com.aisino.base.sysmgr.dbinfo.service.impl;

import com.aisino.base.sysmgr.dbinfo.dao.DbInfoMapper;
import com.aisino.base.sysmgr.dbinfo.entity.DbInfo;
import com.aisino.base.sysmgr.dbinfo.service.DbInfoService;
import com.aisino.common.params.SystemParameter;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.QueryLike.LikeMode;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.sun.rowset.JdbcRowSetImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.JdbcRowSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 为 on 2017-4-27.
 */
@Service("dbInfoService")
public class DbInfoServiceImpl extends BaseServiceImpl<DbInfo, DbInfoMapper> implements DbInfoService {

    @Autowired
    private SystemParameter systemParameter;


    public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";


    @Override
    public boolean addByTaxNo(String taxNo) {
        DbInfo dbInfo = new DbInfo();
        if (this.createDB(taxNo, dbInfo)) {
            this.addEntity(dbInfo);
            return true;
        }
        return false;
    }

    @Override
    public DbInfo getDbInfoByTaxNo(String taxNo) {
        Specification<DbInfo> specification = new Specification<>(DbInfo.class);
        specification.addQueryLike(new QueryLike("taxNo", LikeMode.Eq, taxNo));
        return this.getOne(specification);
    }

    /**
     * 默认数据库
     */
    private boolean createDB(String taxno, DbInfo dbInfo) {
        dbInfo.setTaxNo(taxno);
        dbInfo.setDbname(systemParameter.getDbNamePrefix() + taxno);
        dbInfo.setDbaddr(systemParameter.getDbaddr());
        dbInfo.setDbport(systemParameter.getDbport());
        dbInfo.setDbusr(systemParameter.getUsername());
        dbInfo.setDbpwd(systemParameter.getPassword());
        String url = "jdbc:mysql://" + dbInfo.getDbaddr() + ":" + dbInfo.getDbport() + "/";

        String orderinfoSql = systemParameter.getOrderinfoSql();

        String orderdetailSql = systemParameter.getOrderdetailSql();

        String invoiceinfoSql = systemParameter.getInvoiceinfoSql();

        Connection conn = null;
        Connection newConn = null;

        String dbname = systemParameter.getDbNamePrefix() + taxno;

        try {
            Class.forName(MYSQLDRIVER);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            conn = DriverManager.getConnection(url, dbInfo.getDbusr(), dbInfo.getDbpwd());
            JdbcRowSet jrs = new JdbcRowSetImpl(conn);
            jrs.setCommand("SELECT COUNT(0) as count FROM information_schema.SCHEMATA where SCHEMA_NAME='" + dbname + "'  limit 1");
            jrs.execute();
            while (jrs.next()) {
                if (jrs.getInt(1) > 0) {
                    jrs.close();
                    return true;
                }
            }
            conn = DriverManager.getConnection(url, dbInfo.getDbusr(), dbInfo.getDbpwd());
            String databaseSql = "CREATE database " + dbname + " DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci";

            Statement smt = conn.createStatement();

            smt.execute(databaseSql);
            newConn = DriverManager.getConnection(url + dbname,dbInfo.getDbusr(), dbInfo.getDbpwd());
            if (newConn != null) {

                Statement newSmt = newConn.createStatement();

                newSmt.execute(orderinfoSql);//DDL语句返回值为0;

                newSmt.execute(orderdetailSql);//DDL语句返回值为0;

                newSmt.execute(invoiceinfoSql);//DDL语句返回值为0;

                return true;
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (newConn != null)
                    newConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
