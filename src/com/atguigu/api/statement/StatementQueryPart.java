package com.atguigu.api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

/**
 *  使用statement 查询 t_user表下的全部数据
 */
public class StatementQueryPart {

    /**
     *  TODO:
     *      DriverManager
     *      Connection
     *      Statement
     *      ResultSet
     * @param args
     */

    public static void main(String[] args) throws SQLException {
//        1. 注册驱动:
        /**
         *  TODO:
         *      注册驱动
         *      依赖: 驱动版本 8+  com.mysql.cj.jdbc.Driver
         *      依赖: 驱动版本 5+  com.mysql.jdbc.Driver
         */
        //注册方法为 静态方法
        DriverManager.registerDriver(new Driver());//选择带cj的类 ,然后按alt+enter 抛出异常

//        2. 创建连接: 在java程序和数据库软件中间搭桥
        /**
         * TODO:
         *      java程序,连接数据库,肯定是调用某个方法,这个方法一定也需要填入mysql数据库的一些基本信息
         *          数据库ip地址 127.0.0.1
         *          数据库端口号 3306
         *          账号 root
         *          密码 123456
         *          连接数据库的名称 atguigu-newjdbc
         */


        /**
         *  参数1: url
         *          jdbc:数据库厂商://ip:port/数据库名称
         *          jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc
         *  参数2: username
         *  参数3: password
         *
         */

        //java.sql 接口 = 实现类
         Connection connection= DriverManager.
                 getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc","root","123456");


//        3. 创建发送SQL语句的对象: 在java程序里 创建小车搭载字符串类型的SQL语句
        Statement statement = connection.createStatement();

//        4. 发送SQL语句,获取结果: 发车去往数据库软件,搭载SQL语句执行后的结果回到java程序
        String sql="select * from t_user";
        ResultSet resultSet = statement.executeQuery(sql);//执行该sql语句查询 并返回结果


//        5. 结果解析: resultrest 结果对象,解析集装箱
        //如果有下一行的数据,就继续循环
        while(resultSet.next()){ //和那个游标cursor差不多,每次取一行数据,包含多列
            int id = resultSet.getInt("id");
            String account = resultSet.getString("account");
            String password = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");
            System.out.println(id+"--"+account+"--"+password+"--"+nickname);
        }

//        6. 释放资源: 砸连接,砸小车,砸集装箱
        // 后创建的资源先关闭
        resultSet.close();
        statement.close();
        connection.close();

    }
}
