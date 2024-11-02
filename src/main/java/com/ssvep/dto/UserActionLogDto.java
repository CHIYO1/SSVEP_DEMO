/**
 * 这个文件包含用于用户操作日志数据传输的DTO类
 * 
 * @author 石振山
 * @version 1.0.0
 */
package com.ssvep.dto;

import java.time.LocalDateTime;

public class UserActionLogDto {
    private Long logId;
    private Long userId;
    private String actionType;
    private LocalDateTime timestamp;

    public UserActionLogDto() {}

    public UserActionLogDto(Long logId, Long userId, String actionType, LocalDateTime timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.actionType = actionType;
        this.timestamp = timestamp;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserActionLogDto{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", actionType='" + actionType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
