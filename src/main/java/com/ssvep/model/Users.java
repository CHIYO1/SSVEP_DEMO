/**
 * 这个文件包含Users表的实体类对象
 * 
 * @author 石振山
 * @version 2.0.0
 */
package com.ssvep.model;

import java.util.Map;

public class Users {
    private Long userId;    // 主键，用户的唯一标识
    private String username;    // 用户登录名
    private String password;    // 用户密码
    private String name;    // 用户真实姓名
    private Map<String, Object> preferences;    // 个性化设置，使用JSON格式存储
    private Role role;    // 用户角色

    // 枚举类，用于定义用户角色
    public enum Role {
        USER, ADMIN // 普通用户和管理员
    }

    // 默认构造函数
    public Users() {}

    // 带参数的构造函数
    public Users(String username, String password, String name, Map<String, Object> preferences, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.preferences = preferences;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, Object> preferences) {
        this.preferences = preferences;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // toString方法，用于方便地打印用户信息
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", preferences=" + preferences +
                ", role=" + role +
                '}';
    }

}
