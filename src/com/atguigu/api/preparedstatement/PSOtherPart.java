package com.atguigu.api.preparedstatement;

import org.junit.Test;

import java.sql.*;

/**
 *
 *  练习PS的特殊使用方法:
 *      自增长主键回显
 */
public class PSOtherPart {

    /**
     *  TODO:
     *             t_user插入一条数据,并且获取数据库自增长的主键
     *
     *  TODO:
     *      使用总结:
     *          1.创建preparedStatement的时候,告诉司机,携带回数据库 自增长的主键  (sql , Statement.RETURN_GENERATED_KEYS)
     *          2.获取司机装主键的结果集对象,一行一列,获取对应的数据即可
     *
     */
    @Test
    public void returnPrimaryKey() throws ClassNotFoundException, SQLException {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.建立连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc","root","123456");
        //3.编写SQL语句
        String sql=" Insert into t_user(account,password,nickname)values(?,?,?);";
        //4.创建preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        //5.站位符赋值
        preparedStatement.setObject(1,"test1");
        preparedStatement.setObject(2,"123456");
        preparedStatement.setObject(3,"驴蛋蛋");
        //6.发送SQL语句,并显示结果
        int row = preparedStatement.executeUpdate();

        //7.结果解析
        if(row>0){
            System.out.println("数据插入成功");

            //可以获取 回显的主键
            //获取司机装主键的 结果集对象,一行一列,  id=值
            ResultSet resultSet =preparedStatement.getGeneratedKeys();
            resultSet.next();
            int id=resultSet.getInt(1);
            System.out.println("id="+id);

        }else
            System.out.println("数据插入失败");
        //8.关闭资源
        preparedStatement.close();
        connection.close();

    }


    /**
     *  使用普通的方式 插入 10000条数据
     *
     *  TODO:
     *      使用总结:
     *      1.路径后面添加 "jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc?rewriteBatchedStatements=true"
     *          表示允许批量插入
     *      2.insert into values ,必须是values ,且不能用 ;分号结束
     *      3.是先批量添加到values()后面,最后统一批量执行插入
     *

     */
    @Test
    public void testInsert() throws ClassNotFoundException, SQLException {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.建立连接
        //进行批量插入时,需要在路径后面添加参数
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc?rewriteBatchedStatements=true","root","123456");
        //3.编写SQL语句
        String sql=" Insert into t_user(account,password,nickname)values(?,?,?)";
        //4.创建preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.站位符赋值

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            preparedStatement.setObject(1,"ddd"+i);
            preparedStatement.setObject(2,"ddd"+i);
            preparedStatement.setObject(3,"驴蛋蛋ddd"+i);

            preparedStatement.addBatch();//不执行插入,而是先追加到 values(?,?,?)后面,在循环结束时,执行插入
        }
        preparedStatement.executeBatch();//执行10000条数据 一次性插入
        long end = System.currentTimeMillis();

        //7.结果解析
        System.out.println("执行10000次数据插入 所需的时间"+(end-start));
        //8.关闭资源
        preparedStatement.close();
        connection.close();

    }
}
