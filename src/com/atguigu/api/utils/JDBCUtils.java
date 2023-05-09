package com.atguigu.api.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 *  v1.0版本工具类
 *      内部包含一个连接池对象,并且对外提供获取连接和回收连接的方法
 *   小提示: 推荐使用静态方法, 因为工具类是没有逻辑的,怎么方便怎么来就行了
 *
 *   实现:
 *      属性:
 *          1. 连接池对象(只实例化一次)
 *              (单例设计模式)
 *          2. static{
 *
 *             }
 *
 *
 *      方法:
 *          1.对外提供 连接的方法
 *          2.回收 外部传入连接 的方法
 *
 */


public class JDBCUtils {

    private static DataSource dataSource=null;

    static {
        //1.
        Properties properties=new Properties();
        //2.
        InputStream ips = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
        //3.
        try {
            properties.load(ips);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        try {
            dataSource= DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    /**
     *  对外提供连接的方法
     *
     * @return
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     *  回收 传入的连接
     *
     * @param connection
     * @throws SQLException
     */
    public static void freeConnection(Connection connection) throws SQLException {
        connection.close();
    }

}
