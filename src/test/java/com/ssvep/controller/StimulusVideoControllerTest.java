/**
 * 这个文件用来测试 StimulusVideoController 类是否能正确处理相应请求。
 * 使用了mockito模块模拟 HTTP 请求和响应以及service服务。
 * 
 * @author 石振山
 * @version 3.2.3
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.StimulusVideoDto;
import com.ssvep.service.StimulusVideoService;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class StimulusVideoControllerTest {
    private StimulusVideoController videoController;
    private StimulusVideoService videoService;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        videoService = Mockito.mock(StimulusVideoService.class);
        videoController = new StimulusVideoController();

        videoController.init();
        Field videoControllerField = StimulusVideoController.class.getDeclaredField("videoService");
        videoControllerField.setAccessible(true);
        videoControllerField.set(videoController, videoService);

        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
        resp = Mockito.mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(writer);

        req = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testDoGet_Success() throws ServletException, IOException, SQLException {
        Long id = 1L;
        StimulusVideoDto mockVideoDto = new StimulusVideoDto();
        mockVideoDto.setVideoId(id);
        mockVideoDto.setTestType("test_type");
        mockVideoDto.setVideoUrl("http://example.com/video");

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(videoService.getVideoById(id)).thenReturn(mockVideoDto);

        videoController.doGet(req, resp);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String expectedResponse = objectMapper.writeValueAsString(mockVideoDto);
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoGet_Failure() throws ServletException, IOException, SQLException {
        Long id = 1L;

        when(req.getParameter("id")).thenReturn(String.valueOf(id));
        when(videoService.getVideoById(id)).thenReturn(null);

        videoController.doGet(req, resp);

        String expectedResponse = "{\"error\":\"Video not found\"}";
        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("video_url")).thenReturn("http://example.com/video");

        doNothing().when(videoService).createVideo(any(StimulusVideoDto.class));

        videoController.doPost(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"刺激视频存储成功\"}";

        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPost_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("video_url")).thenReturn("http://example.com/video");

        doThrow(new RuntimeException("刺激视频存储失败")).when(videoService).createVideo(any(StimulusVideoDto.class));

        videoController.doPost(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"刺激视频存储失败\"}";

        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("video_url")).thenReturn("http://example.com/video");

        doNothing().when(videoService).updateVideo(any(StimulusVideoDto.class));

        videoController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"刺激视频更新成功\"}";

        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoPut_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("type")).thenReturn("test_type");
        when(req.getParameter("video_url")).thenReturn("http://example.com/video");

        doThrow(new RuntimeException("刺激视频更新失败")).when(videoService).updateVideo(any(StimulusVideoDto.class));

        videoController.doPut(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"刺激视频更新失败\"}";

        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Success() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");

        doNothing().when(videoService).deleteVideo(anyLong());

        videoController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"success\",\"message\":\"刺激视频删除成功\"}";

        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void testDoDelete_Failure() throws ServletException, IOException, SQLException {
        when(req.getParameter("id")).thenReturn("1");

        doThrow(new RuntimeException("刺激视频删除失败")).when(videoService).deleteVideo(anyLong());

        videoController.doDelete(req, resp);

        String expectedResponse = "{\"status\":\"error\",\"message\":\"刺激视频删除失败\"}";

        writer.flush();
        assertEquals(expectedResponse, outputStream.toString());
    }

}
