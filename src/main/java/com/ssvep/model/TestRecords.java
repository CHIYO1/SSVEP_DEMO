/**
 * 这个文件包含TestRecords表的实体类对象
 * 
 * @author 石振山
 * @version 3.1.1
 */
package com.ssvep.model;

import java.util.Map;
import java.time.*;

public class TestRecords {
    private Long recordId;

    private Long user_id; 

    private String testType;

    private LocalDate testDate; 

    private Map<String, Object> testResults; 

    private String relatedInfo;

    private Long stimulusvideo_id;

    public TestRecords() {}

    public TestRecords(Long user_id, String testType, LocalDate testDate,
            Map<String, Object> testResults, String relatedInfo,
            Long stimulusvideo_id) {
        this.user_id = user_id;
        this.testType = testType;
        this.testDate = testDate;
        this.testResults = testResults;
        this.relatedInfo = relatedInfo;
        this.stimulusvideo_id = stimulusvideo_id;
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

    public java.time.LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(java.time.LocalDate testDate) {
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

    public Long getstimulusvideoId() {
        return stimulusvideo_id;
    }

    public void setstimulusvideoId(Long stimulusvideo_id) {
        this.stimulusvideo_id = stimulusvideo_id;
    }

    @Override
    public String toString() {
        return "TestRecord{" +
                "record_id=" + recordId +
                ", user_id=" + user_id +
                ", test_type='" + testType + '\'' +
                ", test_date=" + testDate +
                ", related_info=" + relatedInfo +
                ", video_id=" + stimulusvideo_id +
                '}';
    }

}
