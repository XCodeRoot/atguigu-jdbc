package com.atguigu.api.druid;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 *
 *
 */
public class DruidUsePart {

    /**
     *  通过读取外部配置文件的方式,实例化druid连接池对象
     *      1.创建Properties对象
     *      2.调用类加载器的方法,获取外部配置文件的输入流
     *      3.导入文件输入流 到properties对象
     *      4.利用连接池 的工具类 的 工厂模式,创建连接池
     *      5.建立连接(getConnection)
     *      6.各种数据库动作
     *      7.回收连接(close)
     */
    @Test
    public void testSoft() throws Exception {
        //1.读取外部配置文件 Properties
        Properties properties=new Properties();

        // src下的文件可以使用类加载器 提供的方法
        // 如果 配置文件放在src里的一个文件夹里,则 路径为 "XX/druid.properties"
        InputStream ips = DruidUsePart.class.getClassLoader().getResourceAsStream("druid.properties");
        //导入文件输入流ips
        properties.load(ips);

        //2.使用连接池的工具类的工厂模式,创建连接池


        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        //建立连接
        Connection connection = dataSource.getConnection();

        //各种数据库操作

        //回收连接
        connection.close();
    }

}
