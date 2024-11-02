/**
 * 这个类负责处理用户操作刺激视频相关的 HTTP 请求并调用 StimulusVideoService 来执行相应的业务逻辑。
 * 
 * @author 石振山
 * @version 2.3.1
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.StimulusVideoDto;
import com.ssvep.service.StimulusVideoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/videos")
public class StimulusVideoController extends HttpServlet {
    private StimulusVideoService videoService;

    @Override
    public void init() throws ServletException {
        videoService = new StimulusVideoService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                StimulusVideoDto videoDto = videoService.getVideoById(id);

                if (videoDto != null) {
                    out.write(objectMapper.writeValueAsString(videoDto));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Video not found\"}");
                }
            } else {
                List<StimulusVideoDto> videoDto = videoService.getAllVideos();
                out.write(objectMapper.writeValueAsString(videoDto));
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"An error occurred while processing the request\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String url = req.getParameter("video_url");

        StimulusVideoDto videoDto = new StimulusVideoDto();
        videoDto.setTestType(type);
        videoDto.setVideoUrl(url);

        try {
            videoService.createVideo(videoDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"刺激视频存储成功\"}");
        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"刺激视频存储失败\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        String url = req.getParameter("video_url");

        StimulusVideoDto videoDto = new StimulusVideoDto();
        videoDto.setVideoId(Long.valueOf(id));
        videoDto.setTestType(type);
        videoDto.setVideoUrl(url);

        try {
            videoService.updateVideo(videoDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"刺激视频更新成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"刺激视频更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String IdParam = req.getParameter("id");
        Long Id = Long.valueOf(IdParam);

        try {
            videoService.deleteVideo(Id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"刺激视频删除成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"刺激视频删除失败\"}");
        }
    }

}
