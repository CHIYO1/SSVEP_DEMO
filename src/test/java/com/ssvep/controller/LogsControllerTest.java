/**
 * 这个文件用来测试 LogsController 类是否能正确处理相应请求。
 * 使用了mockito模块模拟 HTTP 请求和响应以及service服务。
 * 
 * @author 石振山
 * @version 3.2.3
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.UserActionLogDto;
import com.ssvep.service.UserActionLogService;
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
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class LogsControllerTest {
    private LogsController logController;
    private UserActionLogService logService;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        logService = Mockito.mock(UserActionLogService.class);
        logController = new LogsController();

        logController.init();
        Field logControllerField = LogsController.class.getDeclaredField("logService");
        logControllerField.setAccessible(true);
        logControllerField.set(logController, logService);

        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        resp = Mockito.mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(writer);

        req = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testDoGet_Success() throws ServletException, IOException, SQLException {
        Long id = 1L;
        UserActionLogDto mockLogDto = new UserActionLogDto();
        mockLogDto.setLogId(1L);
        mockLogDto.setUserId(1L);
        mockLogDto.setActionType("test_type");
        mockLogDto.setTimestamp(LocalDateTime.now());

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(logService.getLogById(id)).thenReturn(mockLogDto);

        logController.doGet(req, resp);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String expectedResponse = objectMapper.writeValueAsString(mockLogDto);
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoGet_Failure() throws ServletException, IOException, SQLException {
        Long id=1L;

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(logService.getLogById(id)).thenReturn(null);

        logController.doGet(req, resp);

        String expectedResponse = "{\"error\":\"Log not found\"}";
        writer.flush(); 
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Success() throws ServletException,IOException,SQLException{
        when(req.getParameter("user_id")).thenReturn("1");
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("timestamp")).thenReturn(String.valueOf(LocalDateTime.now()));

        doNothing().when(logService).createLog(any(UserActionLogDto.class));

        logController.doPost(req, resp);

        String expectedResponse="{\"status\":\"success\",\"message\":\"日志存储成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Failure() throws ServletException,IOException,SQLException{
        when(req.getParameter("user_id")).thenReturn("1");
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("timestamp")).thenReturn(String.valueOf(LocalDateTime.now()));

        doThrow(new RuntimeException("日志存储失败")).when(logService).createLog(any(UserActionLogDto.class));

        logController.doPost(req, resp);

        String expectedResponse="{\"status\":\"error\",\"message\":\"日志存储失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Success()throws ServletException,IOException,SQLException{
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("user_id")).thenReturn("1");
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("timestamp")).thenReturn(String.valueOf(LocalDateTime.now()));

        doNothing().when(logService).updateLog(any(UserActionLogDto.class));

        logController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"日志更新成功\"}";
        writer.flush(); 
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Failure()throws ServletException,IOException,SQLException{
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("user_id")).thenReturn("1");
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("timestamp")).thenReturn(String.valueOf(LocalDateTime.now()));

        doThrow(new RuntimeException("日志更新失败")).when(logService).updateLog(any(UserActionLogDto.class));

        logController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"日志更新失败\"}";
        writer.flush(); 
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Success()throws ServletException,IOException,SQLException{
        when(req.getParameter("id")).thenReturn("1");

        doNothing().when(logService).deleteLog(anyLong());

        logController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"日志删除成功\"}";
        writer.flush(); 
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Failure()throws ServletException,IOException,SQLException{
        when(req.getParameter("id")).thenReturn("1");

        doThrow(new RuntimeException("日志删除失败")).when(logService).deleteLog(anyLong());

        logController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"日志删除失败\"}";
        writer.flush(); 
        assertEquals(expectedResponse, outputStream.toString());
    }

}
