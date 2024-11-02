/**
 * 这个文件包含StimulusVideos表的实体类对象
 * 
 * @author 石振山
 * @version 2.0.0
 */
package com.ssvep.model;

public class StimulusVideos {
    //主键，唯一标识视频 
    private Long videoId;

    // 检测类型，与检测记录表的 test_type 关联
    private String testType;

    // 视频存储位置的 URL
    private String videoUrl;

    public StimulusVideos() {}

    public StimulusVideos(String testType, String videoUrl) {
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
        return "StimulusVideos{" +
                "videoId=" + videoId +
                ", testType='" + testType + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }

}
