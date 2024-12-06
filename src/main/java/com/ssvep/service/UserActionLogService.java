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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserActionLogService {
    private static final Logger logger = LogManager.getLogger(UserActionLogService.class);
    private UserActionLogsDao logDao;

    public UserActionLogService() {
        logDao = new UserActionLogsDao();
    }

    // 根据ID获取日志
    public UserActionLogDto getLogById(Long id) {
        UserActionLogs log = logDao.getLogById(id);
        if (log != null) {
            logger.info("成功获取日志，日志ID：{}", id);
        } else {
            logger.warn("未找到日志，日志ID：{}", id);
        }
        return convertToDto(log);
    }

    // 根据用户ID获取日志
    public List<UserActionLogDto> getLogsByUser(Long userId) {
        List<UserActionLogs> logs = logDao.getLogsByUser(userId);
        logger.info("成功获取{}条日志，用户ID：{}", logs.size(), userId);
        return logs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 获取所有日志
    public List<UserActionLogDto> getAllLogs() {
        List<UserActionLogs> logs = logDao.getAll();
        logger.info("成功获取{}条日志", logs.size());
        return logs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 创建日志
    public void createLog(UserActionLogDto logDto) throws SQLException {
        UserActionLogs log = convertToEntity(logDto);
        logDao.save(log);
        logger.info("成功创建日志，用户ID：{}", log.getUserId());
    }

    // 更新日志
    public void updateLog(UserActionLogDto logDto) throws SQLException {
        UserActionLogs log = convertToEntity(logDto);
        logDao.update(log);
        logger.info("成功更新日志，日志ID：{}", logDto.getLogId());
    }

    // 删除日志
    public void deleteLog(Long id) throws SQLException {
        logDao.delete(id);
        logger.info("成功删除日志，日志ID：{}", id);
    }

    // DTO到实体的转换
    private UserActionLogDto convertToDto(UserActionLogs log) {
        if (log == null) {
            return null;
        }
        return new UserActionLogDto(log.getLogId(), log.getUserId(), log.getActionType(), log.getTimestamp());
    }

    // 实体到DTO的转换
    private UserActionLogs convertToEntity(UserActionLogDto logDto) {
        UserActionLogs log = new UserActionLogs();
        log.setLogId(logDto.getLogId());
        log.setUserId(logDto.getUserId());
        log.setActionType(logDto.getActionType());
        log.setTimestamp(logDto.getTimestamp());
        return log;
    }
}
