package com.atguigu.api.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcCrudPart {

    public void testInsert() throws SQLException {

        // 连接
        Connection connection = JDBCUtils.getConnection();

        //数据库操作 Crud


        //回收连接
        JDBCUtils.freeConnection(connection);

    }
}
