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

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public UserDto getUserById(Long id) {
        Users user = userDao.getUserById(id);
        return convertToDto(user);
    }

    public void createUser(UserDto userDto) throws SQLException {
        Users user = convertToEntity(userDto);
        userDao.save(user);
    }

    public void updateUser(UserDto userDto) throws SQLException {
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
        return new UserDto(user.getUserId(), user.getUsername(),user.getPassword(), user.getName(), UserDto.Role.valueOf(user.getRole().name()));
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
}
