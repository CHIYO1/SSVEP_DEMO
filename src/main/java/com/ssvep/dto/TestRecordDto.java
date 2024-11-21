/**
 * 这个文件包含用于测试记录数据传输的DTO类
 * 
 * @author 石振山
 * @version 3.1.1
 */
package com.ssvep.dto;

import java.time.LocalDate;
import java.util.Map;

public class TestRecordDto {
    private Long recordId;
    private Long user_id; 
    private String testType;
    private LocalDate testDate; 
    private Map<String, Object> testResults; 
    private String relatedInfo;
    private Long stimulusVideo_id; 

    public TestRecordDto() {}

    public TestRecordDto(Long recordId, Long user_id, String testType, LocalDate testDate,
                         Map<String, Object> testResults, String relatedInfo, Long stimulusVideo_id) {
        this.recordId = recordId;
        this.user_id = user_id;
        this.testType = testType;
        this.testDate = testDate;
        this.testResults = testResults;
        this.relatedInfo = relatedInfo;
        this.stimulusVideo_id = stimulusVideo_id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public Map<String, Object> getTestResults() {
        return testResults;
    }

    public void setTestResults(Map<String, Object> testResults) {
        this.testResults = testResults;
    }

    public String getRelatedInfo() {
        return relatedInfo;
    }

    public void setRelatedInfo(String relatedInfo) {
        this.relatedInfo = relatedInfo;
    }

    public Long getStimulusVideoId() {
        return stimulusVideo_id;
    }

    public void setStimulusVideoId(Long stimulusVideo_id) {
        this.stimulusVideo_id = stimulusVideo_id;
    }

    @Override
    public String toString() {
        return "TestRecordDto{" +
                "recordId=" + recordId +
                ", userId=" + user_id +
                ", testType='" + testType + '\'' +
                ", testDate=" + testDate +
                ", relatedInfo='" + relatedInfo + '\'' +
                ", stimulusVideoId=" + stimulusVideo_id +
                '}';
    }
}
