/**
 * 这个文件用来测试 AnalysisReportController 类是否能正确处理相应请求。
 * 使用了mockito模块模拟 HTTP 请求和响应以及service服务。
 * 
 * @author 石振山
 * @version 2.3.1
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.AnalysisReportDto;
import com.ssvep.service.AnalysisReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AnalysisReportControllerTest {
    private AnalysisReportController reportController;
    private AnalysisReportService reportService;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        reportService = Mockito.mock(AnalysisReportService.class);
        reportController = new AnalysisReportController();

        reportController.init();
        Field reportControllerField = AnalysisReportController.class.getDeclaredField("reportService");
        reportControllerField.setAccessible(true);
        reportControllerField.set(reportController, reportService);

        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        resp = Mockito.mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(writer);

        req = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testDoGet_Success() throws ServletException, IOException {
        Long id = 1L;
        AnalysisReportDto mockReportDto = new AnalysisReportDto();
        mockReportDto.setReportId(id);
        mockReportDto.setTestRecordId(1L);
        mockReportDto.setReportData(new HashMap<>());
        mockReportDto.setCreatedAt(LocalDateTime.now());

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(reportService.getReportById(id)).thenReturn(mockReportDto);

        reportController.doGet(req, resp);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String expectedResponse = objectMapper.writeValueAsString(mockReportDto);
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoGet_Failure() throws ServletException, IOException {
        Long id = 1L;

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(reportService.getReportById(id)).thenReturn(null);

        reportController.doGet(req, resp);

        String expectedResponse = "{\"error\":\"Report not found\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("test_id")).thenReturn("1");
        when(req.getParameter("reportData")).thenReturn("{\"key\":\"value\"}");
        when(req.getParameter("createdAt")).thenReturn(LocalDateTime.now().toString());

        doNothing().when(reportService).createReport(any(AnalysisReportDto.class));

        reportController.doPost(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"分析报告存储成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("test_id")).thenReturn("1");
        when(req.getParameter("reportData")).thenReturn("{\"key\":\"value\"}");
        when(req.getParameter("createdAt")).thenReturn(LocalDateTime.now().toString());

        doThrow(new RuntimeException("分析报告存储失败")).when(reportService).createReport(any(AnalysisReportDto.class));

        reportController.doPost(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"分析报告存储失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("test_id")).thenReturn("1");
        when(req.getParameter("reportData")).thenReturn("{\"key\":\"value\"}");
        when(req.getParameter("createdAt")).thenReturn(LocalDateTime.now().toString());

        doNothing().when(reportService).updateReport(any(AnalysisReportDto.class));

        reportController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"分析报告更新成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("test_id")).thenReturn("1");
        when(req.getParameter("reportData")).thenReturn("{\"key\":\"value\"}");
        when(req.getParameter("createdAt")).thenReturn(LocalDateTime.now().toString());

        doThrow(new RuntimeException("分析报告更新失败")).when(reportService).updateReport(any(AnalysisReportDto.class));

        reportController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"分析报告更新失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");

        doNothing().when(reportService).deleteReport(anyLong());

        reportController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"分析报告删除成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");

        doThrow(new RuntimeException("分析报告删除失败")).when(reportService).deleteReport(anyLong());

        reportController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"分析报告删除失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }
}
