/**
 * 这个类负责处理用户分析报告的业务逻辑，并调用 AnalysisReportsDao 来执行数据访问操作。
 * 
 * @author 石振山
 * @version 2.2.1
 */
package com.ssvep.service;

import com.ssvep.dao.AnalysisReportsDao;
import com.ssvep.dto.AnalysisReportDto;
import com.ssvep.model.AnalysisReports;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AnalysisReportService {
    private AnalysisReportsDao reportsDao;

    public AnalysisReportService() {
        reportsDao = new AnalysisReportsDao();
    }

    public AnalysisReportDto getReportById(Long id) {
        AnalysisReports report = reportsDao.getReportById(id);
        return convertToDto(report);
    }

    public List<AnalysisReportDto> getAllReports() {
        List<AnalysisReports> reports = reportsDao.getAll();

        return reports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void createReport(AnalysisReportDto reportDto) throws SQLException {
        AnalysisReports report = convertToEntity(reportDto);

        reportsDao.save(report);
    }

    public void updateReport(AnalysisReportDto reportDto) throws SQLException {
        AnalysisReports report = convertToEntity(reportDto);

        reportsDao.update(report);
    }

    public void deleteReport(Long id) throws SQLException {
        reportsDao.delete(id);
    }

    private AnalysisReportDto convertToDto(AnalysisReports report) {
        if (report == null) {
            return null;
        }
        return new AnalysisReportDto(report.getReportId(), report.getTestRecordId(), report.getReportData(), report.getCreatedAt());
    }

    private AnalysisReports convertToEntity(AnalysisReportDto reportDto) {
        AnalysisReports report = new AnalysisReports();
        report.setReportId(reportDto.getReportId());
        report.setTestRecordId(reportDto.getTestRecordId());
        report.setReportData(reportDto.getReportData());
        report.setCreatedAt(reportDto.getCreatedAt());
        return report;
    }

}
