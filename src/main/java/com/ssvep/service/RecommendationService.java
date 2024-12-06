/**
 * 这个类负责处理治疗建议的业务逻辑，并调用 TreatmentRecommendationsDao 来执行数据访问操作。
 * 
 * @author 石振山
 * @version 1.1.1
 */
package com.ssvep.service;

import com.ssvep.dao.TreatmentRecommendationsDao;
import com.ssvep.dto.TreatmentRecommendationDto;
import com.ssvep.model.TreatmentRecommendations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationService {
    private static final Logger logger = LogManager.getLogger(RecommendationService.class);
    private TreatmentRecommendationsDao recommendationsDao;

    public RecommendationService() {
        recommendationsDao = new TreatmentRecommendationsDao();
    }

    // 根据建议ID获取治疗建议
    public TreatmentRecommendationDto getrecommendationById(Long id) {
        TreatmentRecommendations recommendation = recommendationsDao.getRecommendationById(id);
        if (recommendation != null) {
            logger.info("成功获取治疗建议，建议ID：{}", id);
        } else {
            logger.warn("未找到治疗建议，建议ID：{}", id);
        }
        return convertToDto(recommendation);
    }

    // 根据用户ID获取治疗建议
    public List<TreatmentRecommendationDto> getrecommendationsByUser(Long userid) {
        List<TreatmentRecommendations> recommendations = recommendationsDao.getRecommendationByUser(userid);
        logger.info("成功获取用户ID {} 的治疗建议，共{}条", userid, recommendations.size());
        return recommendations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 创建治疗建议
    public void createrecommendation(TreatmentRecommendationDto recommendationDto) throws SQLException {
        TreatmentRecommendations recommendation = convertToEntity(recommendationDto);
        recommendationsDao.save(recommendation);
        logger.info("成功创建治疗建议，建议ID：{}", recommendation.getRecommendationId());
    }

    // 更新治疗建议
    public void updateRecommendation(TreatmentRecommendationDto recommendationDto) throws SQLException {
        TreatmentRecommendations recommendation = convertToEntity(recommendationDto);
        recommendationsDao.update(recommendation);
        logger.info("成功更新治疗建议，建议ID：{}", recommendationDto.getRecommendationId());
    }

    // 删除治疗建议
    public void deleteRecommendation(Long id) throws SQLException {
        recommendationsDao.delete(id);
        logger.info("成功删除治疗建议，建议ID：{}", id);
    }

    // 获取所有治疗建议
    public List<TreatmentRecommendationDto> getAllRecommendation() {
        List<TreatmentRecommendations> recommendations = recommendationsDao.getAll();
        logger.info("成功获取所有治疗建议，共{}条", recommendations.size());
        return recommendations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // DTO到实体的转换
    private TreatmentRecommendationDto convertToDto(TreatmentRecommendations recommendations) {
        if (recommendations == null) {
            return null;
        }
        return new TreatmentRecommendationDto(recommendations.getRecommendationId(), recommendations.getUserId(),
                recommendations.getAdvice());
    }

    // 实体到DTO的转换
    private TreatmentRecommendations convertToEntity(TreatmentRecommendationDto TreatmentRecommendationDto) {
        TreatmentRecommendations recommendations = new TreatmentRecommendations();
        recommendations.setRecommendationId(TreatmentRecommendationDto.getRecommendationId());
        recommendations.setUserId(TreatmentRecommendationDto.getUserId());
        recommendations.setAdvice(TreatmentRecommendationDto.getAdvice());
        return recommendations;
    }
}
