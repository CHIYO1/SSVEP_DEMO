/**
 * 这个文件包含UserActionLogs表的DAO类，继承抽象类AbstractDao。
 * 
 * @author 石振山
 * @version 1.1.2
 */
package com.ssvep.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ssvep.model.UserActionLogs;

public class UserActionLogsDao extends AbstractDao<UserActionLogs>{

    @Override
    protected String getTableName() {
        return "useractionlogs";
    }

    @Override
    protected String getIdName() {
        return "log_id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO useractionlogs (user_id, action_type, timestamp) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE useractionlogs SET user_id = ?, action_type = ?, timestamp = ? WHERE log_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, UserActionLogs log) throws SQLException {
        statement.setLong(1, log.getUserId());
        statement.setString(2, log.getActionType());
        statement.setTimestamp(3, Timestamp.valueOf(log.getTimestamp()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, UserActionLogs log) throws SQLException {
        statement.setLong(1, log.getUserId());
        statement.setString(2, log.getActionType());
        statement.setTimestamp(3, Timestamp.valueOf(log.getTimestamp()));
        statement.setLong(4,log.getLogId());
    }

    @Override
    protected void setEntityId(UserActionLogs log, Long id) {
        log.setLogId(id);
    }

    @Override
    protected UserActionLogs mapRowToEntity(ResultSet resultSet) throws SQLException {
        UserActionLogs log=new UserActionLogs();
        log.setLogId(resultSet.getLong("log_id"));
        log.setUserId(resultSet.getLong("user_id"));
        log.setActionType(resultSet.getString("action_type"));
        log.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());

        return log;
    }

    public UserActionLogs getLogById(Long id){
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("log_id", Long.valueOf(id));

        List<UserActionLogs> results = query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results.get(0);
        }
    }

    public List<UserActionLogs> getLogsByUser(Long user_id){
        Map<String,Object> criteria=new HashMap<>();
        criteria.put("user_id",Long.valueOf(user_id));

        List<UserActionLogs> results = query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results;
        }
    }
    
}
