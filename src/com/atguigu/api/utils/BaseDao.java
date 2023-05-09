package com.atguigu.api.utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  封装dao数据库重复代码
 *
 *  TODO:
 *     抽象类 BaseDao 封装两个方法:
 *          一个简化 非DQL
 *          一个简化 DQL(复杂一些,涉及反射和泛型)
 *
 */


public abstract class BaseDao {




    /**
     * 封装简化非DQL语句
     *
     * @param sql   带占位符的sql语句
     * @param params 占位符的值
     * @return  返回执行影响的行数
     */
    public int executeUpdate(String sql,Object...params) throws SQLException {
        //1.建立连接
        Connection connection = JDBCUtilsV2.getConnection();

        //2.创建preparedStatement对象
        PreparedStatement preparedStatement =connection.prepareStatement(sql);

        //3.占位符赋值
        //可变参数 当成数组使用
        for (int i = 1; i <= params.length; i++) {
            preparedStatement.setObject(i,params[i-1]);
        }

        //4.发送SQL语句
        //DML类型
        int row=preparedStatement.executeUpdate();

        //5.关闭资源
        preparedStatement.close();

        //这里需要注意,要判断当前是否是 事务状态
        if (connection.getAutoCommit()) {//为true,则是默认自动事务模式,为false则为开启事务
            JDBCUtilsV2.freeConnection();
        }//开启了事务,则不在此处处理

        return row;
    }

//-------------------------------------------------------------------------



    /**
     *  DQL语句封装方法 -> 返回值 是什么类型的呢? 答案是 泛型 List<T>
     *              并不是 List<Map> map key和value 可以自定义
     *                              map 没有数据校验机制:检查正负等
     *                              map 不支持反射,持久层全是反射
     *
     *              数据库数据->java实体类
     *
     *              table t_user表
     *                属性:
     *                  id
     *                  account
     *                  password
     *                  nickname
     *
     *              java User类
     *                属性:
     *                  id
     *                  account
     *                  password
     *                  nickname
     *              表中->一行->java实体类的一个对象 -> 多行 -> List<Java实体类> list;
     *
     *
     *  <T> 声明一个泛型,不确定类型
     *      1.确定泛型 User.class T= User
     *      2.要使用反射技术属性赋值
     *     //  这个T表示声明泛型类型T
     *     //          这个T 表示返回值类型是T泛型集合
     *     //                            这个T 反射实例化对象,为属性赋值
     *    public <T> List<T> executeQuery(Class<T> clazz,String sql,Object...params){
     *
     *    }
     *
     */


    /**
     *  将查询结果封装到 一个 实体类集合!!
     * @param clazz 要接值的实体类集合 的模板对象
     * @param sql 查询语句 ,要求 列名或者属性名 = 实体类的属性名
     * @param params 占位符的值 要和?位置对应
     * @param <T> 声明的结果的泛型
     * @return 查询的实体类集合
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object...params) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        //1.获取连接
        Connection connection = JDBCUtilsV2.getConnection();

        //2.创建preparedStatement,并且传入SQL语句结构
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //3.占位符赋值
        if(params!=null&&params.length!=0){//判断 传入的参数 是否为空
            for (int i = 1; i <= params.length; i++) {
                preparedStatement.setObject(i,params[i-1]);
            }
        }



        //4.发送SQL语句
        ResultSet resultSet = preparedStatement.executeQuery();

        //5.解析结果集

        
        List<T> list=new ArrayList<>();

        //获取列的信息对象
        //TODO: metaData 装的是当前结果集 列的信息对象! (可以根据下标 获取到 列名 ,还可以获取列的数量)
        ResultSetMetaData metaData = resultSet.getMetaData();

        //有了它以后,我们可以水平遍历 列名
        int columnCount = metaData.getColumnCount();

        while(resultSet.next()){
            T t=clazz.newInstance();//调用类的无参构造函数 实例化对象
            //一行数据 一个T类型对象



            //自动遍历
            for (int i = 1; i <= columnCount; i++) {


                // metaData可以 根据下标 获取 列名和列数量

                //对象的属性名
                String propertyName = metaData.getColumnLabel(i);
                //对象的属性值
                Object value = resultSet.getObject(i);

                //反射给对象的属性值 赋值
                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);//属性可以设置打破private私有修饰
                //参数1 表示要赋值的对象 ,参数2 表示具体的属性值
                field.set(t,value);
            }

            list.add(t);
        }

        //关闭资源
        resultSet.close();
        preparedStatement.close();
        if(connection.getAutoCommit()){
            //没有事务 ,可以关闭
            JDBCUtilsV2.freeConnection();
        }
        return list;
    }

}
