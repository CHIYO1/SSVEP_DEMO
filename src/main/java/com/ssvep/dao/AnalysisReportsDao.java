/**
 * 这个文件包含AnalysisReports表的DAO类，继承抽象类AbstractDao。
 * 
 * @author 石振山
 * @version 2.1.2
 */
package com.ssvep.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssvep.model.AnalysisReports;
import com.ssvep.util.JsonConverter;


public class AnalysisReportsDao extends AbstractDao<AnalysisReports>{

    @Override
    protected String getTableName() {
        return "analysisreports";
    }

    @Override
    protected String getIdName() {
        return "report_id";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO analysisreports (record_id, report_data, created_at) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE analysisreports SET record_id=?,report_data=?,created_at=? WHERE report_id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, AnalysisReports report) throws SQLException {
        statement.setLong(1, report.getTestRecordId());
        statement.setString(2, JsonConverter.convertToJson(report.getReportData()));
        statement.setDate(3, Date.valueOf(report.getCreatedAt().toLocalDate()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, AnalysisReports report) throws SQLException {
        statement.setLong(1, report.getTestRecordId());
        statement.setString(2, JsonConverter.convertToJson(report.getReportData()));
        statement.setDate(3, Date.valueOf(report.getCreatedAt().toLocalDate()));
        statement.setLong(4, report.getReportId());
    }

    @Override
    protected void setEntityId(AnalysisReports report, Long id) {
        report.setReportId(id);
    }

    @Override
    protected AnalysisReports mapRowToEntity(ResultSet resultSet) throws SQLException {
        AnalysisReports report=new AnalysisReports();

        report.setReportId(resultSet.getLong("report_id"));
        report.setReportData(JsonConverter.convertToMap(resultSet.getString("report_data")));
        report.setCreatedAt(resultSet.getDate("created_at").toLocalDate().atStartOfDay());

        return report;
    }

    public AnalysisReports getReportById(Long id){
        Map<String,Object> criteria=new HashMap<>();

        criteria.put("report_id", id);
        List<AnalysisReports> results=query(criteria);

        if(results.isEmpty()){
            return null;
        }else{
            return results.get(0);
        }
    }
    
}
