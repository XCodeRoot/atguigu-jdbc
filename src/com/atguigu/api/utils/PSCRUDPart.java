package com.atguigu.api.utils;


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
public class PSCRUDPart extends BaseDao {//继承BaseDao,可以拥有BaseDao的executeUpdate()和executeQuery()方法

    @Test //导入测试包 ,junit4 , 先@Test 再对报错的 用alt+enter,导入junit4包
    public void testInsert() throws ClassNotFoundException, SQLException {
        /**
         *  这里就变得非常简单了 , 保留sql语句,然后直接调用父类的 executeUpdate(sql语句,可变参数)方法
         *
         */

        String sql="Insert into t_user(account,password,nickname)values(?,?,?)";

        int i = executeUpdate(sql, "测试333,3333,ergouzi");
        System.out.println("i = " + i);

    }

    @Test
    public void testUpdate() throws ClassNotFoundException, SQLException {

        String sql="update t_user set nickname=? where id=?  ;";

        int row = executeUpdate(sql, "新的nickname", 3);
    }

    @Test
    public void testDelete() throws ClassNotFoundException, SQLException {

        String sql="delete from  t_user where id=? ;";
        int i = executeUpdate(sql, 3);
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
