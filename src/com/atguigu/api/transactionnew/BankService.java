package com.atguigu.api.transactionnew;


import com.atguigu.api.utils.JDBCUtilsV2;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 *  银行卡业务方法,调用Dao
 */
public class BankService {

    @Test
    public void start() throws SQLException, ClassNotFoundException {
        transfer("lvdandan","ergouzi",500);
    }


    /**
     *  TODO:
     *      事务添加是在业务方法中
     *      利用try catch 代码块,开启事务 和 关闭事务,和事务回滚
     *      将connection传入Dao层即可!!,不能在Dao层close()
     *
     *
     * @param addAccount
     * @param subAccount
     * @param money
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void transfer(String addAccount,String subAccount, int money) throws SQLException, ClassNotFoundException {
        BankDao bankDao = new BankDao();
        //一个事务的最基本的要求 ,必须是同一个连接对象 connection
        //事务的开启 是在业务层
        //一个转账方法 属于一个事务 内含(加钱,减钱)

        //调用工具类,获取连接
        Connection connection = JDBCUtilsV2.getConnection();

        try {
            //开启事务
            //关闭事务自动提交!
            connection.setAutoCommit(false);

            //执行数据库动作 ,,业务 事务需要在同一个连接里,所以把connection传下去
            bankDao.add(addAccount,money);
            System.out.println("-------------");
            bankDao.sub(subAccount,money);


            //事务提交
            connection.commit();
        } catch (Exception e) {
            //事务回滚
            connection.rollback();

            //回滚以后再抛出异常
            throw e;
        }finally {
            JDBCUtilsV2.freeConnection();
        }


    }

}
