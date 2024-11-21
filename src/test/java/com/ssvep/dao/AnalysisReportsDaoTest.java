/**
 * 这个文件包含一个对AnalysisReportsDao类进行测试的类，
 * 测试是否可以正常对进行AnalysisReports表进行增删改查操作。
 * 
 * @author 石振山
 * @version 2.2.3
 */
package com.ssvep.dao;

import org.junit.jupiter.api.*;
import com.ssvep.model.AnalysisReports;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class AnalysisReportsDaoTest {
    private AnalysisReportsDao reportsDao;

    @BeforeEach
    public void setUp() {
        reportsDao = new AnalysisReportsDao(); 
    }

    @Test
    public void testSaveReport() {
        LocalDateTime time = LocalDateTime.now();

        AnalysisReports report = new AnalysisReports(7L,null,time);

        reportsDao.save(report);

        AnalysisReports retrievedReport = reportsDao.getReportById(report.getReportId());


        assertNotNull(retrievedReport, "Report should be saved and retrievable");
        assertEquals(report.getReportId(), retrievedReport.getReportId(), "report IDs should match");
    }

    @Test
    public void testGetAllReports() {
        LocalDateTime time = LocalDateTime.now();

        AnalysisReports report1 = new AnalysisReports(7L,null,time);

        reportsDao.save(report1);

        AnalysisReports report2 = new AnalysisReports(7L,null,time);

        reportsDao.save(report2);

        List<AnalysisReports> reports = reportsDao.getAll();
        assertEquals(reports.size()>=2, true ,"There should be 2 reports in the database");
    }

    @Test
    public void testUpdateReport() {
        LocalDateTime time = LocalDateTime.now();

        AnalysisReports report = new AnalysisReports(7L,null,time);

        reportsDao.save(report);

        report.setReportData(new HashMap<>());
        reportsDao.update(report);

        AnalysisReports updatedReport = reportsDao.getReportById(report.getReportId());
        assertEquals(report.getReportData(), updatedReport.getReportData(), "Report data should be updated");
    }

    @Test
    public void testDeleteReport() {
        LocalDateTime time = LocalDateTime.now();

        AnalysisReports report = new AnalysisReports(7L,null,time);

        reportsDao.save(report);

        reportsDao.delete(report.getReportId());

        AnalysisReports deletedReport = reportsDao.getReportById(report.getReportId());
        assertNull(deletedReport, "Report should be deleted and not retrievable");
    }

}
