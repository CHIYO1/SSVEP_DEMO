/**
 * 这个文件包含AnalysisReports表的实体类对象
 * 
 * @author 石振山
 * @version 3.1.2
 */
package com.ssvep.model;

import java.util.Map;
import java.time.*;

public class AnalysisReports {
    private Long reportId;

    private Long testRecord_id; 

    private Map<String, Object> reportData;

    private LocalDateTime createdAt; 

    public AnalysisReports() {}

    public AnalysisReports(Long testRecord_id, Map<String, Object> reportData, LocalDateTime createdAt) {
        this.testRecord_id = testRecord_id;
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
        return testRecord_id;
    }

    public void setTestRecordId(Long testRecord_id) {
        this.testRecord_id = testRecord_id;
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
        return "AnalysisReport{" +
                "report_id=" + reportId +
                ",record_id=" + testRecord_id +
                ",report_data=" + reportData +
                ",created_at=" + createdAt +
                '}';
    }
}
