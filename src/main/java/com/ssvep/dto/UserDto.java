/**
 * 这个文件包含用于用户数据传输的DTO类
 * 
 * @author 石振山
 * @version 2.1.2
 */
package com.ssvep.dto;

public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private String name;
    private Role role;

    public enum Role {
        USER, ADMIN 
    }

    public UserDto() {}

    public UserDto(Long userId, String username, String password, String name, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
