package com.atguigu.api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * 模拟用户登录
 *  TODO:
 *      1.明确jdbc的使用流程 和 详细将解内部设计api
 *      2.发现问题,引出preparedStatement
 *
 *  TODO:
 *      输入账号和密码
 *      进入数据库查询t_user
 *      反馈登录成功还是失败
 *
 *  TODO:
 *      1.键盘输入事件 :账号和密码
 *      2.注册驱动
 *      3.获取连接
 *      4.创建statement
 *      5.发送查询SQL语句,并返回结果
 *      6.结果判断,成功还是失败
 *      7.关闭资源
 */
public class StatementUserLoginPart {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//---------1.键盘输入事件 :账号和密码
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入账号:");
        String account=scanner.nextLine();
        System.out.println("请输入密码:");
        String password = scanner.nextLine();

//------------2.注册驱动
        /**
         *  方案1:
         *      DriverManager.registerDriver(new Driver());
         *      问题: 注册了两次驱动
         *      1.DriverManager.registerDriver()方法本身会注册一次驱动
         *      2.Driver.static{ DriverManager.registerDriver() } 静态代码块,也会注册一次驱动
         *    解决: 只想注册一次驱动,只触发静态代码块就好了
         *    触发静态代码块 :
         *          类加载机制:类加载的时候,会触发静态代码块
         *                   加载[class->jvm虚拟机的class对象]
         *                   连接[验证(检查文件类型)->准备(静态变量默认值)->解析(触发静态代码块)]
         *                   初始化(静态属性赋真实值)
         *    触发类加载:
         *      1.new 关键字
         *      2.调用静态方法
         *      3.调用静态属性
         *      4.接口 1.8 default默认实现
         *      5.反射
         *      6.子类触发父类
         *      7.程序的入口 main
         *
         */
            //方案1
            //DriverManager.registerDriver(new Driver());

            //方案2 : 此处是mysql版本的驱动,后期将无法改为oracle版本,这种是死代码
            //new Driver();

            //方案3: 反射
            // 字符串 -> 提取到外部配置文件 -> 可以在不改变代码的情况下,完成数据库的切换!!这个非常好
        Class.forName("com.mysql.cj.jdbc.Driver");//反射 触发静态代码块 ,完成 驱动注册 ,同样要抛alt+enter异常




//-----------3.获取连接 getConnection(1,2,3)方法为重载方法,参数可变
        //三个参数的方法(最优最简)
        Connection connection =DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc","root","123456");

        //两个参数的方法
        Properties properties=new Properties();
        properties.put("user","root");
        properties.put("password","123456");
        Connection connection1 =DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc",properties);

        //三个参数的方法
        Connection connection2 =DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc?user=root&password=123456");


//----------4.创建发送SQL语句的对象
        Statement statement =connection.createStatement();
//-----------5.创建和发送SQl语句
        String sql="SELECT * From t_user where account= '"+account+"' AND PASSWORD ='"+password+"'; ";

        /**
         *    SQL语句分类:
         *      DDL(容器创建,修改,删除)
         *      DML(插入,修改,删除)
         *      DQL(查询)
         *      DCL(权限控制)
         *      TPL(事务控制语言)
         *    参数: sql 非DQL
         *    返回: int
         *        1)DML 返回操作影响的行数
         *        2)非DQl return 0;
         *
         *    参数: sql DQL
         *    返回: resultSet结果封装对象
         *    ResultSet resultSet = statement.executeQuery(sql);
         *  int row=statement.executeUpdate(sql);//返回修改的行数
         */
        //int i=statement.executeUpdate(sql);
        ResultSet resultSet = statement.executeQuery(sql);


//--------6.解析结果集

        //.next()可以返回布尔类型的同时,进行游标后移
//        while(resultSet.next()){
//            int id = resultSet.getInt(1);
//            String account1 = resultSet.getString("account");
//            String password1 = resultSet.getString(3);
//            String nickname = resultSet.getString("nickname");
//            System.out.println(id+"--"+account1+"--"+password1+"--"+nickname);
//        }


        //这里可以不用while ,因为上面的SQL语句查询的 条件就是where account="root"
        //所以只要查询有结果,则表明账号和密码符合数据库比对
        if(resultSet.next()){
            System.out.println("登录成功");
        }else {
            System.out.println(
                    "登录失败"
            );
        }

//----------7.关闭资源
        resultSet.close();
        statement.close();
        connection.close();

    }
}
