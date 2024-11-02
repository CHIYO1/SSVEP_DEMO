/**
 * 这个文件包含UserActionLogs表的实体类对象
 * 
 * @author 石振山
 * @version 2.0.0
 */
package com.ssvep.model;

import java.time.LocalDateTime; 

public class UserActionLogs {
    // 主键，唯一标识操作日志
    private Long logId;

    // 外键，关联用户表
    private Long userId; 
    // 操作类型
    private String actionType;

    // 操作时间
    private LocalDateTime timestamp;

    public UserActionLogs() {}

    public UserActionLogs(Long userId, String actionType, LocalDateTime timestamp) {
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
        return "UserActionLogs{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", actionType='" + actionType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
