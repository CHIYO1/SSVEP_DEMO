/**
 * 这个文件包含一个数据库配置类，用于加载和管理数据库连接的配置。
 * 
 * @author 石振山
 * @version 1.0.0
 */
package com.ssvep.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private Properties properties; // 存储配置属性的对象

    public DatabaseConfig() {
        properties = new Properties();
        loadProperties(); // 加载配置文件
    }

    /**
     * 加载 application.properties 配置文件中的属性。
     */
    private void loadProperties() {
        // 使用 try-with-resources 语法自动关闭输入流
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(input); // 从输入流中加载属性
        } catch (IOException ex) {
            ex.printStackTrace(); // 打印异常信息
        }
    }

    /**
     * 获取数据库 URL。
     *
     * @return 数据库 URL 字符串
     */
    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    /**
     * 获取数据库用户名。
     *
     * @return 数据库用户名
     */
    public String getDbUser() {
        return properties.getProperty("db.user");
    }

    /**
     * 获取数据库密码。
     *
     * @return 数据库密码
     */
    public String getDbPassword() {
        return properties.getProperty("db.password");
    }

}
