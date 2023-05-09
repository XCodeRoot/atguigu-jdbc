package com.atguigu.api.preparedstatement;


import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *  使用 preparedStatement 对 t_user表 进行 crud
 */
public class PSCRUDPart {

    @Test //导入测试包 ,junit4 , 先@Test 再对报错的 用alt+enter,导入junit4包
    public void testInsert() throws ClassNotFoundException, SQLException {
        /**
         *  插入一条数据
         *      account test
         *      password test
         *      nickname 二狗子
         */
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.建立连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc","root","123456");
        //3.1 编写SQL语句结果 ,动态值部分用 ? 代替
        String sql="Insert into t_user(account,password,nickname)values(?,?,?)";
        //3.2 创建preparedStatement,并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //3.3 占位符赋值
        preparedStatement.setObject(1,"test");
        preparedStatement.setObject(2,"test");
        preparedStatement.setObject(3,"二狗子");
        //4.发送SQL语句
        //DML类型
        int row = preparedStatement.executeUpdate();
        //5.输出结果
        if(row>0){
            System.out.println("数据插入成功");
        }else {
            System.out.println("数据插入失败");
        }
        //6.关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, SQLException {
        /**
         *  修改 id=3 的 nickname为 三狗子
         */
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.建立连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc", "root", "123456");
        //3.1 编写SQL语句结果 ,动态值部分用 ? 代替
        String sql="update t_user set nickname=? where id=?  ;";
        //3.2 创建preparedStatement,并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //3.3 占位符赋值
        preparedStatement.setObject(1,"三狗子");
        preparedStatement.setObject(2,3);
        //4.发送SQL语句
        //DML类型
        int row = preparedStatement.executeUpdate();
        //5.输出结果
        if(row>0){
            System.out.println("修改成功");
        }else
            System.out.println("修改失败");
        //6.关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testDelete() throws ClassNotFoundException, SQLException {
        /**
         *  删除id=3的用户数据
         */
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.建立连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc", "root", "123456");
        //3.1 编写SQL语句结果 ,动态值部分用 ? 代替
        String sql="delete from  t_user where id=? ;";
        //3.2 创建preparedStatement,并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //3.3 占位符赋值
        preparedStatement.setObject(1,3);
        //4.发送SQL语句
        //DML类型
        int row = preparedStatement.executeUpdate();
        //5.输出结果
        if(row>0){
            System.out.println("数据删除成功");
        }else
            System.out.println("数据删除失败");
        //6.关闭资源
        preparedStatement.close();
        connection.close();
    }


    /**
     *  查询所有用户的数据,并且封装到一个 List<Map> list集合中
     *
     *      解释:
     *        id  account password nickname
     *        id  account password nickname
     *        id  account password nickname
     *      数据库 -> resultSet -> java -> 一行 - map(key=列名,value=列值) -> List<Map> list
     *      也就是说,一行数据存在 一个map里, 把所有map又装进一个list集合里
     *  难点:如何获取列名
     */
    @Test
    public void testSelect() throws SQLException, ClassNotFoundException {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.建立连接
        Connection connection = DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu-newjdbc", "root", "123456");
        //3.1 编写SQL语句结果 ,动态值部分用 ? 代替
        String sql="select id,account,password,nickname from t_user;";
        //3.2 创建preparedStatement,并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //3.3 占位符赋值
        //无
        //4.发送SQL语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //5.解析结果集=
        List<Map> list=new ArrayList<>();

        //获取列的信息对象
        //TODO: metaData 装的是当前结果集 列的信息对象! (可以根据下标 获取到 列名 ,还可以获取列的数量)
        ResultSetMetaData metaData = resultSet.getMetaData();

        //有了它以后,我们可以水平遍历 列名
        int columnCount = metaData.getColumnCount();

        while(resultSet.next()){
            Map map = new HashMap<>();
            //一行数据 一个map
            //纯手动取值
//            map.put("id",resultSet.getInt("id"));
//            map.put("account",resultSet.getString("account"));
//            map.put("password",resultSet.getString("password"));
//            map.put("nickname",resultSet.getString("nickname"));

            //自动遍历
            for (int i = 1; i <= columnCount; i++) {
                // metaData可以 根据下标 获取 列名和列数量
                String columnLabel = metaData.getColumnLabel(i);//获取指定列名
                Object value = resultSet.getObject(i);//获取指定列值
                map.put(columnLabel,value);
            }

            list.add(map);
        }
        System.out.println("list="+list);
        //6.关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
