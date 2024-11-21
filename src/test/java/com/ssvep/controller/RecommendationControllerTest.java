/**
 * 这个文件用来测试 RecommendationController 类是否能正确处理相应请求。
 * 使用了 Mockito 模块模拟 HTTP 请求和响应以及 RecommendationService 服务。
 * 
 * @version 2.1.2
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssvep.dto.TreatmentRecommendationDto;
import com.ssvep.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RecommendationControllerTest {
    private RecommendationController recommendationController;
    private RecommendationService recommendationService;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;
    

    @BeforeEach
    public void setUp() throws Exception {
        recommendationService = Mockito.mock(RecommendationService.class);
        recommendationController = new RecommendationController();

        recommendationController.init();
        Field recommendationControllerField = RecommendationController.class.getDeclaredField("recommendationService");
        recommendationControllerField.setAccessible(true);
        recommendationControllerField.set(recommendationController, recommendationService);

        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        resp = Mockito.mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(writer);

        req = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testDoGet_Success() throws ServletException, IOException {
        Long id = 1L;
        TreatmentRecommendationDto mockRecommendationDto = new TreatmentRecommendationDto();
        mockRecommendationDto.setRecommendationId(id);
        mockRecommendationDto.setUserId(2L);
        mockRecommendationDto.setAdvice(Collections.singletonMap("advice", "null"));

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(recommendationService.getrecommendationById(id)).thenReturn(mockRecommendationDto);

        recommendationController.doGet(req, resp);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(mockRecommendationDto);
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoGet_Failure() throws ServletException, IOException {
        Long id = 1L;

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(recommendationService.getrecommendationById(id)).thenReturn(null);

        recommendationController.doGet(req, resp);

        String expectedResponse = "{\"error\":\"Recommendation not found\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("user_id")).thenReturn("2");
        when(req.getParameter("advice")).thenReturn("{\"key\":\"value\"}");

        doNothing().when(recommendationService).createrecommendation(any(TreatmentRecommendationDto.class));

        recommendationController.doPost(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"治疗建议添加成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("user_id")).thenReturn("2");
        when(req.getParameter("advice")).thenReturn("{\"key\":\"value\"}");

        doThrow(new RuntimeException("治疗建议添加失败")).when(recommendationService).createrecommendation(any(TreatmentRecommendationDto.class));

        recommendationController.doPost(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"治疗建议添加失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("user_id")).thenReturn("2");
        when(req.getParameter("advice")).thenReturn("{\"key\":\"value\"}");

        doNothing().when(recommendationService).updateRecommendation(any(TreatmentRecommendationDto.class));

        recommendationController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"治疗建议更新成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("user_id")).thenReturn("2");
        when(req.getParameter("advice")).thenReturn("{\"key\":\"value\"}");

        doThrow(new RuntimeException("治疗建议更新失败")).when(recommendationService).updateRecommendation(any(TreatmentRecommendationDto.class));

        recommendationController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"治疗建议更新失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");

        doNothing().when(recommendationService).deleteRecommendation(anyLong());

        recommendationController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"治疗建议删除成功\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");

        doThrow(new RuntimeException("治疗建议删除失败")).when(recommendationService).deleteRecommendation(anyLong());

        recommendationController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"治疗建议删除失败\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }
}
