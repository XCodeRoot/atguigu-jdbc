package com.atguigu.api.preparedstatement;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * TODO:
 *  防止注入攻击
 *  PreparedStatement用户注册
 *
 */
public class PSUserLoginPart {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//---------键盘输入事件 :账号和密码
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入账号:");
        String account=scanner.nextLine();
        System.out.println("请输入密码:");
        String password = scanner.nextLine();

//-------1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

//-------2.获取连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://localhost/atguigu-newjdbc","root","123456");

        /**
         *
         *  preparedstatement
         *      1.编写SQL语句结果  不包含动态值的语句,动态值部分用占位符问号?代替 , 注意 ? 只能用来代替动态值
         *      2.创建preparedstatement对象,并且传入动态值
         *      3.动态值 占位符 赋值 问号? 单独赋值即可
         *      4.发送SQL语句,并返回结果集
         */
//-------3.1 编写SQL语句结果
        String sql="SELECT * From t_user where account=? AND password = ?;";
//-------3.2 创建预编译PreparedStatement对象 并设置SQl语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//-------3.3 单独的占位符 赋值
        preparedStatement.setObject(1,account);
        preparedStatement.setObject(2,password);

//-------4.发送SQL语句 并返回结果集
        //此时 .executeQuery或者.executeUpdate都不需要传入sql语句,因为preparStatement已经知道了sql语句和其动态值
        ResultSet resultSet = preparedStatement.executeQuery();
//-------5.解析结果集
        if(resultSet.next()){
            System.out.println("登录成功");
        }else {
            System.out.println(
                    "登录失败"
            );
        }
//-------6.关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }

}
