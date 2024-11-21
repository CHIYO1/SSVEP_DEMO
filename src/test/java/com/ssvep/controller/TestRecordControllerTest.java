/**
 * 这个文件用来测试 TestRecordController 类是否能正确处理相应请求。
 * 使用了mockito模块模拟 HTTP 请求和响应以及service服务。
 * 
 * @author 石振山
 * @version 4.3.1
 */

package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.TestRecordDto;
import com.ssvep.service.TestRecordService;
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
import java.time.LocalDate;
import java.util.Collections;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TestRecordControllerTest {
    private TestRecordController testRecordController;
    private TestRecordService recordService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        recordService = Mockito.mock(TestRecordService.class);
        testRecordController = new TestRecordController();

        testRecordController.init();
        Field recordServiceField = TestRecordController.class.getDeclaredField("recordService");
        recordServiceField.setAccessible(true);
        recordServiceField.set(testRecordController, recordService);

        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        response = Mockito.mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(writer);

        request = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testDoGet_Success() throws ServletException, IOException {
        Long recordId = 1L;
        TestRecordDto mockRecordDto = new TestRecordDto();
        mockRecordDto.setRecordId(recordId);
        mockRecordDto.setUserId(2L);
        mockRecordDto.setTestType("Blood Test");
        mockRecordDto.setTestDate(LocalDate.now());
        mockRecordDto.setTestResults(Collections.singletonMap("result", "positive"));

        when(request.getParameter("id")).thenReturn(String.valueOf(recordId));
        when(recordService.getRecordById(recordId)).thenReturn(mockRecordDto);

        testRecordController.doGet(request, response);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String expectedResponse = objectMapper.writeValueAsString(mockRecordDto);
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoGet_Failure() throws ServletException, IOException {
        Long recordId = 1L;

        when(request.getParameter("id")).thenReturn(String.valueOf(recordId));
        when(recordService.getRecordById(recordId)).thenReturn(null);

        testRecordController.doGet(request, response);

        String expectedResponse = "{\"error\":\"Record not found\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Success() throws ServletException, IOException, SQLException {
        String userId = "2";
        String testType = "Blood Test";
        String testDate = LocalDate.now().toString();
        String testResults = "{\"result\":\"positive\"}";
        String relatedInfo = "N/A";
        String stimulusVideoId = "1";

        when(request.getParameter("user_id")).thenReturn(userId);
        when(request.getParameter("test_type")).thenReturn(testType);
        when(request.getParameter("test_date")).thenReturn(testDate);
        when(request.getParameter("test_results")).thenReturn(testResults);
        when(request.getParameter("related_info")).thenReturn(relatedInfo);
        when(request.getParameter("video_id")).thenReturn(stimulusVideoId);

        doNothing().when(recordService).createRecord(any(TestRecordDto.class));

        testRecordController.doPost(request, response);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"检测记录存储成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Failure() throws ServletException, IOException, SQLException {
        String userId = "2";
        String testType = "Blood Test";
        String testDate = LocalDate.now().toString();
        String testResults = "{\"result\":\"positive\"}";
        String relatedInfo = "N/A";
        String stimulusVideoId = "1";

        when(request.getParameter("user_id")).thenReturn(userId);
        when(request.getParameter("test_type")).thenReturn(testType);
        when(request.getParameter("test_date")).thenReturn(testDate);
        when(request.getParameter("test_results")).thenReturn(testResults);
        when(request.getParameter("related_info")).thenReturn(relatedInfo);
        when(request.getParameter("video_id")).thenReturn(stimulusVideoId);

        doThrow(new RuntimeException("检测记录存储失败")).when(recordService).createRecord(any(TestRecordDto.class));

        testRecordController.doPost(request, response);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"检测记录存储失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Success() throws ServletException, IOException, SQLException {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("user_id")).thenReturn("2");
        when(request.getParameter("test_type")).thenReturn("Blood Test");
        when(request.getParameter("test_date")).thenReturn(LocalDate.now().toString());
        when(request.getParameter("test_results")).thenReturn("{\"result\":\"positive\"}");
        when(request.getParameter("related_info")).thenReturn("N/A");
        when(request.getParameter("video_id")).thenReturn("1");

        doNothing().when(recordService).updateRecord(any(TestRecordDto.class));

        testRecordController.doPut(request, response);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"检测记录更新成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Failure() throws ServletException, IOException, SQLException {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("user_id")).thenReturn("2");
        when(request.getParameter("test_type")).thenReturn("Blood Test");
        when(request.getParameter("test_date")).thenReturn(LocalDate.now().toString());
        when(request.getParameter("test_results")).thenReturn("{\"result\":\"positive\"}");
        when(request.getParameter("related_info")).thenReturn("N/A");
        when(request.getParameter("video_id")).thenReturn("1");

        doThrow(new RuntimeException("检测记录更新失败")).when(recordService).updateRecord(any(TestRecordDto.class));

        testRecordController.doPut(request, response);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"检测记录更新失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Success() throws ServletException, IOException, SQLException {
        when(request.getParameter("id")).thenReturn("1");

        doNothing().when(recordService).deleteRecord(anyLong());

        testRecordController.doDelete(request, response);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"检测记录删除成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Failure() throws ServletException, IOException, SQLException {
        when(request.getParameter("id")).thenReturn("1");

        doThrow(new RuntimeException("检测记录删除失败")).when(recordService).deleteRecord(anyLong());

        testRecordController.doDelete(request, response);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"检测记录删除失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }
}
