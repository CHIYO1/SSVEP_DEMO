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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class); // 日志记录器
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public UserDto getUserById(Long id) {
        logger.info("获取用户信息，用户ID: {}", id);
        Users user = userDao.getUserById(id);
        if (user != null) {
            logger.info("成功获取用户信息，用户ID: {}", id);
        } else {
            logger.warn("未找到用户信息，用户ID: {}", id);
        }
        return convertToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        logger.info("获取用户信息，用户名: {}", username);
        Users user = userDao.getUserByUsername(username);
        if (user != null) {
            logger.info("成功获取用户信息，用户名: {}", username);
        } else {
            logger.warn("未找到用户信息，用户名: {}", username);
        }
        return convertToDto(user);
    }

    public void createUser(UserDto userDto) throws SQLException {
        logger.info("创建用户，用户名: {}", userDto.getUsername());
        String pw = userDto.getPassword();
        String passwordHash = BCrypt.hashpw(pw, BCrypt.gensalt());
        userDto.setPassword(passwordHash);
        Users user = convertToEntity(userDto);

        userDao.save(user);
        logger.info("成功创建用户，用户名: {}", userDto.getUsername());
    }

    public void updateUser(UserDto userDto) throws SQLException {
        logger.info("更新用户，用户名: {}", userDto.getUsername());
        String pw = userDto.getPassword();
        String passwordHash = BCrypt.hashpw(pw, BCrypt.gensalt());
        userDto.setPassword(passwordHash);
        Users user = convertToEntity(userDto);

        userDao.update(user);
        logger.info("成功更新用户，用户名: {}", userDto.getUsername());
    }

    public void deleteUser(Long id) throws SQLException {
        logger.info("删除用户，用户ID: {}", id);
        userDao.delete(id);
        logger.info("成功删除用户，用户ID: {}", id);
    }

    public List<UserDto> getAllUsers() {
        logger.info("获取所有用户信息");
        List<Users> users = userDao.getAll();
        List<UserDto> userDtos = users.stream().map(this::convertToDto).collect(Collectors.toList());
        logger.info("成功获取所有用户信息，数量: {}", userDtos.size());
        return userDtos;
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

        String passwordHash = userDto.getPassword();
        // 使用 BCrypt 来验证密码
        return BCrypt.checkpw(password, passwordHash);
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