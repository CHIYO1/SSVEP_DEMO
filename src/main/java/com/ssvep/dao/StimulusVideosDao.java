/**
 * 这个文件包含StimulusVideos表的DAO类，继承抽象类AbstractDao。
 * 
 * @author 石振山
 * @version 2.1.1
 */
package com.ssvep.dao;

import com.ssvep.model.StimulusVideos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StimulusVideosDao extends AbstractDao<StimulusVideos>{

    @Override
    protected String getTableName() {
        return "stimulusvideos";
    }

    @Override
    protected String getIdName() {
        return "video_id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO stimulusvideos (test_type, video_url) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE stimulusvideos SET test_type = ?, video_url = ? WHERE video_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, StimulusVideos video) throws SQLException {
        statement.setString(1, video.getTestType());
        statement.setString(2, video.getVideoUrl());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, StimulusVideos video) throws SQLException {
        statement.setString(1, video.getTestType());
        statement.setString(2, video.getVideoUrl());
        statement.setLong(3, video.getVideoId());
    }

    @Override
    protected void setEntityId(StimulusVideos video, Long id) {
        video.setVideoId(id);
    }

    @Override
    protected StimulusVideos mapRowToEntity(ResultSet resultSet) throws SQLException {
        StimulusVideos video=new StimulusVideos();

        video.setVideoId(resultSet.getLong("video_id"));
        video.setTestType(resultSet.getString("test_type"));
        video.setVideoUrl(resultSet.getString("video_url"));

        return video;
    }
    
    public StimulusVideos getVideoById(Long id){
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("video_id", Long.valueOf(id));

        List<StimulusVideos> results = query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results.get(0);
        }
    }

}
