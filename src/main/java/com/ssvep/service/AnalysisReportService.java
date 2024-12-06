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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AnalysisReportService {
    private static final Logger logger = LogManager.getLogger(AnalysisReportService.class);
    private AnalysisReportsDao reportsDao;

    public AnalysisReportService() {
        reportsDao = new AnalysisReportsDao();
    }

    // 根据报告ID获取分析报告
    public AnalysisReportDto getReportById(Long id) {
        AnalysisReports report = reportsDao.getReportById(id);
        if (report != null) {
            logger.info("成功获取分析报告，报告ID：{}", id);
        } else {
            logger.warn("未找到分析报告，报告ID：{}", id);
        }
        return convertToDto(report);
    }

    // 获取所有分析报告
    public List<AnalysisReportDto> getAllReports() {
        List<AnalysisReports> reports = reportsDao.getAll();
        logger.info("成功获取所有分析报告，共{}条", reports.size());
        return reports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 创建分析报告
    public void createReport(AnalysisReportDto reportDto) throws SQLException {
        AnalysisReports report = convertToEntity(reportDto);
        reportsDao.save(report);
        logger.info("成功创建分析报告，报告ID：{}", report.getReportId());
    }

    // 更新分析报告
    public void updateReport(AnalysisReportDto reportDto) throws SQLException {
        AnalysisReports report = convertToEntity(reportDto);
        reportsDao.update(report);
        logger.info("成功更新分析报告，报告ID：{}", reportDto.getReportId());
    }

    // 删除分析报告
    public void deleteReport(Long id) throws SQLException {
        reportsDao.delete(id);
        logger.info("成功删除分析报告，报告ID：{}", id);
    }

    // DTO到实体的转换
    private AnalysisReportDto convertToDto(AnalysisReports report) {
        if (report == null) {
            return null;
        }
        return new AnalysisReportDto(report.getReportId(), report.getTestRecordId(), report.getReportData(), report.getCreatedAt());
    }

    // 实体到DTO的转换
    private AnalysisReports convertToEntity(AnalysisReportDto reportDto) {
        AnalysisReports report = new AnalysisReports();
        report.setReportId(reportDto.getReportId());
        report.setTestRecordId(reportDto.getTestRecordId());
        report.setReportData(reportDto.getReportData());
        report.setCreatedAt(reportDto.getCreatedAt());
        return report;
    }
}
