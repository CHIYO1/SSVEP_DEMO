/**
 * 这个文件包含用于分析报告数据传输的DTO类
 * 
 * @author 石振山
 * @version 1.1.2
 */
package com.ssvep.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class AnalysisReportDto {
    private Long reportId;
    private Long testRecordId;
    private Map<String, Object> reportData;
    private LocalDateTime createdAt;

    public AnalysisReportDto() {}

    public AnalysisReportDto(Long reportId, Long testRecordId, Map<String, Object> reportData, LocalDateTime createdAt) {
        this.reportId = reportId;
        this.testRecordId = testRecordId;
        this.reportData = reportData;
        this.createdAt = createdAt;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getTestRecordId() {
        return testRecordId;
    }

    public void setTestRecordId(Long testRecordId) {
        this.testRecordId = testRecordId;
    }

    public Map<String, Object> getReportData() {
        return reportData;
    }

    public void setReportData(Map<String, Object> reportData) {
        this.reportData = reportData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AnalysisReportDto{" +
                "reportId=" + reportId +
                ", testRecordId=" + testRecordId +
                ", reportData=" + reportData +
                ", createdAt=" + createdAt +
                '}';
    }
}
