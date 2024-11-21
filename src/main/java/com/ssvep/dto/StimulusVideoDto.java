/**
 * 这个文件包含用于刺激视频数据传输的DTO类
 * 
 * @author 石振山
 * @version 2.0.0
 */
package com.ssvep.dto;

public class StimulusVideoDto {
    private Long videoId;
    private String testType;
    private String videoUrl;

    public StimulusVideoDto() {}

    public StimulusVideoDto(Long videoId, String testType, String videoUrl) {
        this.videoId = videoId;
        this.testType = testType;
        this.videoUrl = videoUrl;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "StimulusVideoDto{" +
                "videoId=" + videoId +
                ", testType='" + testType + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
