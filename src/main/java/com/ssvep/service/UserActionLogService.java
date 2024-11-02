/**
 * 这个类负责处理用户日志的业务逻辑，并调用 UserActionLogDao 来执行数据访问操作。
 * 
 * @author 石振山
 * @version 1.2.1
 */
package com.ssvep.service;

import com.ssvep.dao.UserActionLogsDao;
import com.ssvep.dto.UserActionLogDto;
import com.ssvep.model.UserActionLogs;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserActionLogService {
    private UserActionLogsDao logDao;

    public UserActionLogService() {
        logDao = new UserActionLogsDao();
    }

    public UserActionLogDto getLogById(Long id) {
        UserActionLogs log = logDao.getLogById(id);
        return convertToDto(log);
    }

    public List<UserActionLogDto> getLogsByUser(Long userid) {
        List<UserActionLogs> logs = logDao.getLogsByUser(userid);

        return logs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<UserActionLogDto> getAllLogs() {
        List<UserActionLogs> logs = logDao.getAll();

        return logs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void createLog(UserActionLogDto logDto) throws SQLException {
        UserActionLogs log = convertToEntity(logDto);

        logDao.save(log);
    }

    public void updateLog(UserActionLogDto logDto) throws SQLException {
        UserActionLogs log = convertToEntity(logDto);

        logDao.update(log);
    }

    public void deleteLog(Long id) throws SQLException {
        logDao.delete(id);
    }

    private UserActionLogDto convertToDto(UserActionLogs log) {
        if (log == null) {
            return null;
        }
        return new UserActionLogDto(log.getLogId(), log.getUserId(), log.getActionType(), log.getTimestamp());
    }

    private UserActionLogs convertToEntity(UserActionLogDto logDto) {
        UserActionLogs log = new UserActionLogs();
        log.setLogId(logDto.getLogId());
        log.setUserId(logDto.getUserId());
        log.setActionType(logDto.getActionType());
        log.setTimestamp(logDto.getTimestamp());
        return log;
    }

}
