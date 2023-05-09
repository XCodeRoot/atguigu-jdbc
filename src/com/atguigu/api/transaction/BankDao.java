package com.atguigu.api.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 *  表的数据库操作方法存储类
 */
public class BankDao {

    /**
     *  加钱的数据库操作方法(jdbc)
     *
     * @param account
     * @param money
     */
    public void add(String account,int money,Connection connection) throws ClassNotFoundException, SQLException {
        //1.因为连接由业务层 开启,所以Dao里不能再 进行连接

        //3.编写SQL
        String sql="update t_bank set money=money+? where account=?;";
        //4.创建preparedStatement小车
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        preparedStatement.setObject(1,money);
        preparedStatement.setObject(2,account);
        //6.发送sql
        int row = preparedStatement.executeUpdate();
        //7.关闭资源
        preparedStatement.close();
        //connection.close();//由业务层统一管理
        System.out.println("加钱成功");

    }


    /**
     *  减钱的数据库操作方法
     * @param account
     * @param money
     */
    public void sub (String account,int money,Connection connection) throws ClassNotFoundException, SQLException {
        //1.因为连接由业务层 开启,所以Dao里不能再 进行连接


        //3.编写SQL
        String sql="update t_bank set money=money-? where account=?;";
        //4.创建preparedStatement小车
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        preparedStatement.setObject(1,money);
        preparedStatement.setObject(2,account);
        //6.发送sql
        int row = preparedStatement.executeUpdate();
        //7.关闭资源
        preparedStatement.close();
        //connection.close();//这里也不能关闭连接,因为需要让业务层统一管理
        System.out.println("减钱成功");
    }

}
