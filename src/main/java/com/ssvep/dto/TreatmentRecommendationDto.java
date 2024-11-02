/**
 * 这个文件包含用于治疗建议数据传输的DTO类
 * 
 * @author 石振山
 * @version 2.0.0
 */
package com.ssvep.dto;

import java.util.Map;

public class TreatmentRecommendationDto {
    private Long recommendationId;
    private Long userId;
    private Map<String, Object> advice;

    public TreatmentRecommendationDto() {
    }

    public TreatmentRecommendationDto(Long recommendationId, Long userId, Map<String, Object> advice) {
        this.recommendationId = recommendationId;
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
        return "TreatmentRecommendationDto{" +
                "recommendationId=" + recommendationId +
                ", userId=" + userId +
                ", advice=" + advice +
                '}';
    }
}
