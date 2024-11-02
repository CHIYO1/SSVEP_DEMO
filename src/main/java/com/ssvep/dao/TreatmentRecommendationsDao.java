/**
 * 这个文件包含TreatmentRecommendations表的DAO类，继承抽象类AbstractDao。
 * 
 * @author 石振山
 * @version 1.0.0
 */
package com.ssvep.dao;

import com.ssvep.model.TreatmentRecommendations;
import com.ssvep.util.JsonConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreatmentRecommendationsDao extends AbstractDao<TreatmentRecommendations> {

    @Override
    protected String getTableName() {
        return "treatmentrecommendations";
    }

    @Override
    protected String getIdName() {
        return "recommendation_id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO treatmentrecommendations (user_id,advice) VALUES(?,?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE treatmentrecommendations SET user_id= ?, advice= ? WHERE recommendation_id= ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, TreatmentRecommendations recommendation)
            throws SQLException {
        statement.setLong(1, recommendation.getUserId());
        statement.setString(2, JsonConverter.convertToJson(recommendation.getAdvice()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, TreatmentRecommendations recommendation)
            throws SQLException {
        statement.setLong(1, recommendation.getUserId());
        statement.setString(2, JsonConverter.convertToJson(recommendation.getAdvice()));
        statement.setLong(3, recommendation.getRecommendationId());
    }

    @Override
    protected void setEntityId(TreatmentRecommendations recommendation, Long id) {
        recommendation.setRecommendationId(id);
    }

    @Override
    protected TreatmentRecommendations mapRowToEntity(ResultSet resultSet) throws SQLException {
        TreatmentRecommendations recommendation=new TreatmentRecommendations();

        recommendation.setRecommendationId(resultSet.getLong("recommendation_id"));
        recommendation.setUserId(resultSet.getLong("user_id"));
        recommendation.setAdvice(JsonConverter.convertToMap(resultSet.getString("advice")));

        return recommendation;
    }

    public TreatmentRecommendations getRecommendationById(Long id){
        Map<String,Object> criteria=new HashMap<>();
        criteria.put("recommendation_id", id);

        List<TreatmentRecommendations> results=query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results.get(0);
        }
    }

    public List<TreatmentRecommendations> getRecommendationByUser(Long user_id){
        Map<String,Object> criteria=new HashMap<>();
        criteria.put("user_id", user_id);

        List<TreatmentRecommendations> results=query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results;
        }
    }

}
