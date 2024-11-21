/**
 * 这个文件包含Users表的DAO类，继承抽象类AbstractDao。
 * 
 * @author 石振山
 * @version 1.2.2
 */
package com.ssvep.dao;

import com.ssvep.model.Users;
import com.ssvep.util.JsonConverter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao extends AbstractDao<Users> {

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected String getIdName() {
        return "user_id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO users (username, password, name, preferences, role) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE users SET username = ?, password = ?, name = ?, preferences = ?, role = ? WHERE user_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, Users user) throws SQLException {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getName());
        statement.setString(4, JsonConverter.convertToJson(user.getPreferences()));
        statement.setString(5, user.getRole().name());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Users user) throws SQLException {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getName());
        statement.setString(4, JsonConverter.convertToJson(user.getPreferences()));
        statement.setString(5, user.getRole().name());
        statement.setLong(6, user.getUserId());
    }

    @Override
    protected void setEntityId(Users user, Long id) {
        user.setUserId(id);
    }

    @Override
    protected Users mapRowToEntity(ResultSet resultSet) throws SQLException {
        Users user = new Users();
        user.setUserId(resultSet.getLong("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setPreferences(JsonConverter.convertToMap(resultSet.getString("preferences")));
        user.setRole(Users.Role.valueOf(resultSet.getString("role").toUpperCase()));
        return user;
    }

    public Users getUserById(Long id){
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("user_id", Long.valueOf(id));

        List<Users> results = query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results.get(0);
        }
    }

    public Users getUserByUsername(String username){
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("username", username);

        List<Users> results = query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results.get(0);
        }
    }

}
