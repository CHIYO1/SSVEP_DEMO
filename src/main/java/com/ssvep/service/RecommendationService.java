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

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationService {
    private TreatmentRecommendationsDao recommendationsDao;

    public RecommendationService() {
        recommendationsDao = new TreatmentRecommendationsDao();
    }

    public TreatmentRecommendationDto getrecommendationById(Long id) {
        TreatmentRecommendations recommendation = recommendationsDao.getRecommendationById(id);
        return convertToDto(recommendation);
    }

    public List<TreatmentRecommendationDto> getrecommendationsByUser(Long userid) {
        List<TreatmentRecommendations> recommendations = recommendationsDao.getRecommendationByUser(userid);

        return recommendations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void createrecommendation(TreatmentRecommendationDto recommendationDto) throws SQLException {
        TreatmentRecommendations recommendation = convertToEntity(recommendationDto);
        recommendationsDao.save(recommendation);
    }

    public void updateRecommendation(TreatmentRecommendationDto recommendationDto) throws SQLException {
        TreatmentRecommendations recommendation = convertToEntity(recommendationDto);
        recommendationsDao.update(recommendation);
    }

    public void deleteRecommendation(Long id) throws SQLException {
        recommendationsDao.delete(id);
    }

    public List<TreatmentRecommendationDto> getAllRecommendation() {
        List<TreatmentRecommendations> recommendations = recommendationsDao.getAll();
        return recommendations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private TreatmentRecommendationDto convertToDto(TreatmentRecommendations recommendations) {
        if (recommendations == null) {
            return null;
        }
        return new TreatmentRecommendationDto(recommendations.getRecommendationId(), recommendations.getUserId(),
                recommendations.getAdvice());
    }

    private TreatmentRecommendations convertToEntity(TreatmentRecommendationDto TreatmentRecommendationDto) {
        TreatmentRecommendations recommendations = new TreatmentRecommendations();
        recommendations.setRecommendationId(TreatmentRecommendationDto.getRecommendationId());
        recommendations.setUserId(TreatmentRecommendationDto.getUserId());
        recommendations.setAdvice(TreatmentRecommendationDto.getAdvice());
        return recommendations;
    }

}
