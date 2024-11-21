/**
 * 这个文件包含一个数据库连接类，使用单例模式管理数据库连接实例。
 * 
 * @author 石振山
 * @version 1.2.1
 */
package com.ssvep.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance; // 单例实例
    private Connection connection; // 数据库连接对象
    private DatabaseConfig config; // 数据库配置对象

    /**
     * 私有构造函数，初始化数据库连接。
     * 构造函数中注册 JDBC 驱动并创建连接。
     */
    private DatabaseConnection() {
        config = new DatabaseConfig(); // 加载数据库配置
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            // 创建数据库连接
            connection = DriverManager.getConnection(
                    config.getDbUrl(), 
                    config.getDbUser(), 
                    config.getDbPassword() 
            );
            connection.setAutoCommit(false);  // 禁用自动提交,每次操作进行手动提交
            System.out.println("数据库连接成功！");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到 JDBC 驱动！" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("数据库连接失败！" + e.getMessage());
        }
    }

    /**
     * 获取数据库连接的单例实例。
     *
     * @return DatabaseConnection 的实例
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection(); 
        }
        return instance; 
    }

    /**
     * 获取当前数据库连接。
     *
     * @return 当前的数据库连接
     */
    public Connection getConnection() {
        return connection; 
    }

    /**
     * 关闭数据库连接。
     * 在不再需要连接时，可以调用此方法关闭连接。
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close(); 
            } catch (SQLException e) {
                e.printStackTrace(); 
            }
        }
    }

}
