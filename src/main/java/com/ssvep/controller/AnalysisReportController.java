/**
 * 这个类负责处理用户操作分析报告相关的 HTTP 请求并调用 AnalysisReportService 来执行相应的业务逻辑。
 *
 * @author 石振山
 * @version 2.3.2
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.AnalysisReportDto;
import com.ssvep.service.AnalysisReportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet("/analysisreport")
public class AnalysisReportController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AnalysisReportController.class);
    private AnalysisReportService reportService;

    @Override
    public void init() throws ServletException {
        reportService = new AnalysisReportService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        logger.info("收到 GET 请求, 参数: id={}", idParam);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                AnalysisReportDto reportDto = reportService.getReportById(id);

                if (reportDto != null) {
                    logger.info("成功获取分析报告，报告ID：{}", id);
                    out.write(objectMapper.writeValueAsString(reportDto));
                } else {
                    logger.warn("未找到分析报告，报告ID：{}", id);
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Report not found\"}");
                }
            } else {
                List<AnalysisReportDto> reportDto = reportService.getAllReports();
                logger.info("成功获取所有分析报告，共 {} 条", reportDto.size());
                out.write(objectMapper.writeValueAsString(reportDto));
            }

        } catch (Exception e) {
            logger.error("处理 GET 请求时发生错误: {}", e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"An error occurred while processing the request\"}");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String testRecordId = req.getParameter("test_id");
        String reportData = req.getParameter("reportData");
        String createdAt = req.getParameter("createdAt");

        logger.info("收到 POST 请求, 参数: test_id={}, createdAt={}", testRecordId, createdAt);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> reportMap = objectMapper.readValue(reportData, Map.class);

        AnalysisReportDto reportDto = new AnalysisReportDto();
        reportDto.setTestRecordId(Long.valueOf(testRecordId));
        reportDto.setReportData(reportMap);
        reportDto.setCreatedAt(LocalDateTime.parse(createdAt));

        try {
            reportService.createReport(reportDto);
            logger.info("成功创建分析报告, TestRecordID: {}", testRecordId);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"分析报告存储成功\"}");
        } catch (Exception e) {
            logger.error("创建分析报告时发生错误: {}", e.getMessage(), e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"分析报告存储失败\"}");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String testRecordId = req.getParameter("test_id");
        String reportData = req.getParameter("reportData");
        String createdAt = req.getParameter("createdAt");

        logger.info("收到 PUT 请求, 参数: id={}, test_id={}, createdAt={}", id, testRecordId, createdAt);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> reportMap = objectMapper.readValue(reportData, Map.class);

        AnalysisReportDto reportDto = new AnalysisReportDto();
        reportDto.setReportId(Long.valueOf(id));
        reportDto.setTestRecordId(Long.valueOf(testRecordId));
        reportDto.setReportData(reportMap);
        reportDto.setCreatedAt(LocalDateTime.parse(createdAt));

        try {
            reportService.updateReport(reportDto);
            logger.info("成功更新分析报告, 报告ID: {}", id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"分析报告更新成功\"}");
        } catch (Exception e) {
            logger.error("更新分析报告时发生错误: {}", e.getMessage(), e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"分析报告更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        logger.info("收到 DELETE 请求, 参数: id={}", idParam);

        Long id = Long.valueOf(idParam);

        try {
            reportService.deleteReport(id);
            logger.info("成功删除分析报告, 报告ID: {}", id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"分析报告删除成功\"}");
        } catch (Exception e) {
            logger.error("删除分析报告时发生错误: {}", e.getMessage(), e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"分析报告删除失败\"}");
        }
    }
}
