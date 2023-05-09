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
 *  TODO:
 *      通过ThreadLocal线程本地变量存储连接,确保一个线程的多个方法可以获取到同一个connection
 *      优势: 事务操作的时候 Service和 Dao 属于一个线程,不用再传递参数了
 *      大家都可以调用 getConnection() ,自动获取的是同一个连接
 */


public class JDBCUtilsV2 {

    private static DataSource dataSource=null;

    //创建线程本地变量
    private static ThreadLocal<Connection> tl=new ThreadLocal<>();


    static {
        //1.
        Properties properties=new Properties();
        //2.
        InputStream ips = JDBCUtilsV2.class.getClassLoader().getResourceAsStream("druid.properties");
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

        Connection connection = tl.get();
        if(connection==null){
            //线程本地变量如果没有 连接,
            connection = dataSource.getConnection();
            tl.set(connection);//存入线程本地变量
        }
        return connection;
    }

    /**
     *  回收 传入的连接
     *
     * @throws SQLException
     */
    public static void freeConnection() throws SQLException {

        Connection connection = tl.get();
        if(connection!=null){
            tl.remove();//清空线程本地变量
            connection.setAutoCommit(true);//事务状态回归 , 原本是自动事务,为了开启事务,传入false,关闭事务自然需要重设为true
            connection.close();//回收
        }

    }

}
