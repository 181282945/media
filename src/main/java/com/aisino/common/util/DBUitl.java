package com.aisino.common.util;

/**
 * Created by 为 on 2017-4-27.
 */
import com.aisino.common.params.SystemParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//@Component("dBUitl")
public class DBUitl {

//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        String database = "test2";
//        new DBUitl().createDB(database);
//    }

    private static final String mysqlDriver = "com.mysql.jdbc.Driver";
    /**
     * 默认数据库
     */
    private static final String defaultDb = "mysql";


//
//    @Autowired
//    private SystemParameter systemParameter;
//
//    public void createDB(String taxno) {
////        jdbc:mysql://localhost:3306/
//        String url = "jdbc:mysql://" + systemParameter.getDbaddr() + ":" + systemParameter.getDbport() + "/";
//        String username = systemParameter.getUsername();
//        String password = systemParameter.getPassword();
//
//        String orderinfoSql = systemParameter.getOrderinfoSql();
//
//        Connection conn = null;
//        Connection newConn = null;
//
//        String dbname =  systemParameter.getDbNamePrefix() + taxno;
//
//        try {
//            Class.forName(mysqlDriver);
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        try {
////            String tableSql = "create table t_user (username varchar(50) not null primary key,"
////                    + "password varchar(20) not null ); ";
//            String databaseSql = "create database " + dbname;
//
//            conn = DriverManager.getConnection(url + defaultDb, username, password);
//            Statement smt = conn.createStatement();
//            if (conn != null) {
//                System.out.println("数据库连接成功!");
//
//                smt.executeUpdate(databaseSql);
//
//                newConn = DriverManager.getConnection(url + dbname,
//                        username, password);
//                if (newConn != null) {
//                    System.out.println("已经连接到新创建的数据库：" + dbname);
//
//                    Statement newSmt = newConn.createStatement();
//                    int i = newSmt.executeUpdate(orderinfoSql);//DDL语句返回值为0;
//                    if (i == 0) {
//                        System.out.println("orderinfoSql 表已经创建成功!");
//                    }
//                }
//            }
//
//        } catch (SQLException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }finally {
//            try {
//                conn.close();
//                newConn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}