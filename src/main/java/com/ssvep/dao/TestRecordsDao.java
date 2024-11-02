/**
 * 这个文件包含TestRecords表的DAO类，继承抽象类AbstractDao。
 * 
 * @author 石振山
 * @version 2.1.1
 */
package com.ssvep.dao;

import com.ssvep.model.TestRecords;
import com.ssvep.util.JsonConverter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRecordsDao extends AbstractDao<TestRecords>{

    @Override
    protected String getTableName() {
        return "testrecords";
    }

    @Override
    protected String getIdName() {
        return "record_id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO testrecords (user_id, test_type, test_date, test_results, related_info, video_id) VALUES (?, ?, ?, ?, ?,?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE testrecords SET user_id = ?, test_type = ?, test_date = ?, test_results = ?, related_info = ?, video_id =? WHERE record_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, TestRecords record) throws SQLException {
        statement.setLong(1, record.getUserId());
        statement.setString(2,record.getTestType());
        statement.setDate(3, Date.valueOf(record.getTestDate()));
        statement.setString(4, JsonConverter.convertToJson(record.getTestResults()));
        statement.setString(5, record.getRelatedInfo());
        statement.setLong(6, record.getstimulusvideoId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, TestRecords record) throws SQLException {
        statement.setLong(1, record.getUserId());
        statement.setString(2,record.getTestType());
        statement.setDate(3, Date.valueOf(record.getTestDate()));
        statement.setString(4, JsonConverter.convertToJson(record.getTestResults()));
        statement.setString(5, record.getRelatedInfo());
        statement.setLong(6, record.getstimulusvideoId());
        statement.setLong(7, record.getRecordId());
    }

    @Override
    protected void setEntityId(TestRecords record, Long id) {
        record.setRecordId(id);
    }

    @Override
    protected TestRecords mapRowToEntity(ResultSet resultSet) throws SQLException {
        TestRecords record = new TestRecords();
        
        Long user_id=resultSet.getLong("user_id");
        record.setUserId(user_id);

        record.setRecordId(resultSet.getLong("record_id"));
        record.setTestType(resultSet.getString("test_type"));

        Date sqlDate = resultSet.getDate("test_date");
        record.setTestDate(sqlDate != null ? sqlDate.toLocalDate() : null);

        record.setTestResults(JsonConverter.convertToMap(resultSet.getString("test_results")));
        record.setRelatedInfo(resultSet.getString("related_info"));

        Long video_id=resultSet.getLong("video_id");
        record.setstimulusvideoId(video_id);

        return record;
    }

    public TestRecords getRecordById(Long id){
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("record_id", Long.valueOf(id));

        List<TestRecords> results = query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results.get(0);
        }
    }

    public List<TestRecords> getRecordsByUser(Long user_id){
        Map<String,Object> criteria=new HashMap<>();
        criteria.put("user_id",Long.valueOf(user_id));

        List<TestRecords> results = query(criteria);
        if(results.isEmpty()){
            return null;
        }else{
            return results;
        }
    }
    
}
