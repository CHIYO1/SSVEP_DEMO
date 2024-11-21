/**
 * 这个文件包含TreatmentRecommendations表的实体类对象
 * 
 * @author 石振山
 * @version 2.0.0
 */
package com.ssvep.model;

import java.util.Map;

public class TreatmentRecommendations {
    // 主键，唯一标识治疗建议
    private Long recommendationId;

    // 外键，关联用户表
    private Long userId;

    // 治疗建议内容，使用 JSON 格式存储
    private Map<String, Object> advice;

    public TreatmentRecommendations() {
    }

    public TreatmentRecommendations(Long userId, Map<String, Object> advice) {
        this.userId = userId;
        this.advice = advice;
    }

    public Long getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<String, Object> getAdvice() {
        return advice;
    }

    public void setAdvice(Map<String, Object> advice) {
        this.advice = advice;
    }

    @Override
    public String toString() {
        return "TreatmentRecommendations{" +
                "recommendationId=" + recommendationId +
                ", userId=" + userId +
                ", advice=" + advice +
                '}';
    }

}
