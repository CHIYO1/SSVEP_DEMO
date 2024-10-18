/**
 * 这个文件包含一个数据库连接类，使用单例模式管理数据库连接实例。
 * 
 * @author 石振山
 * @version 1.0.0
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
            Class.forName("com.mysql.cj.jdbc.Driver"); // 根据使用的数据库修改驱动类
            // 创建数据库连接
            connection = DriverManager.getConnection(
                    config.getDbUrl(), // 从配置中获取数据库 URL
                    config.getDbUser(), // 从配置中获取数据库用户名
                    config.getDbPassword() // 从配置中获取数据库密码
            );
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
            instance = new DatabaseConnection(); // 创建新的实例
        }
        return instance; // 返回单例实例
    }

    /**
     * 获取当前数据库连接。
     *
     * @return 当前的数据库连接
     */
    public Connection getConnection() {
        return connection; // 返回数据库连接
    }

    /**
     * 关闭数据库连接。
     * 在不再需要连接时，可以调用此方法关闭连接。
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close(); // 关闭数据库连接
            } catch (SQLException e) {
                e.printStackTrace(); // 打印异常信息
            }
        }
    }

}
