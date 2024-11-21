/**
 * 这个类负责处理用户的业务逻辑，并调用 UserDao 来执行数据访问操作。
 * 
 * @author 石振山
 * @version 1.1.1
 */
package com.ssvep.service;

import com.ssvep.dao.UserDao;
import com.ssvep.dto.UserDto;
import com.ssvep.model.Users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.mindrot.jbcrypt.BCrypt;
import java.security.Key;

public class UserService {
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public UserDto getUserById(Long id) {
        Users user = userDao.getUserById(id);
        return convertToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        Users user = userDao.getUserByUsername(username);
        return convertToDto(user);
    }

    public void createUser(UserDto userDto) throws SQLException {
        String pw = userDto.getPassword();
        String PasswordHash = BCrypt.hashpw(pw, BCrypt.gensalt());
        userDto.setPassword(PasswordHash);
        Users user = convertToEntity(userDto);
        userDao.save(user);
    }

    public void updateUser(UserDto userDto) throws SQLException {
        String pw = userDto.getPassword();
        String PasswordHash = BCrypt.hashpw(pw, BCrypt.gensalt());
        userDto.setPassword(PasswordHash);
        Users user = convertToEntity(userDto);
        userDao.update(user);
    }

    public void deleteUser(Long id) throws SQLException {
        userDao.delete(id);
    }

    public List<UserDto> getAllUsers() {
        List<Users> users = userDao.getAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private UserDto convertToDto(Users user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getUserId(), user.getUsername(), user.getPassword(), user.getName(),
                UserDto.Role.valueOf(user.getRole().name()));
    }

    private Users convertToEntity(UserDto userDto) {
        Users user = new Users();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setRole(Users.Role.valueOf(userDto.getRole().name()));
        return user;
    }

    public boolean authenticate(String username, String password) {
        UserDto userDto = getUserByUsername(username);

        if (userDto == null) {
            return false; // 用户名不存在
        }

        String PasswordHash = userDto.getPassword();
        // 使用 BCrypt 来验证密码
        return BCrypt.checkpw(password, PasswordHash);
    }

    // 生成密钥
    private Key getSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // 生成 JWT 令牌
    public String generateToken(String username) {
        long expirationTime = 43200000;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 使用 Key 对象和算法
                .compact();
    }
}
