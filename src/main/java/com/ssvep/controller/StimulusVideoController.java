/**
 * 这个类负责处理用户操作刺激视频相关的 HTTP 请求并调用 StimulusVideoService 来执行相应的业务逻辑。
 * 
 * @author 石振山
 * @version 2.3.1
 */
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger(StimulusVideoService.class);
    private StimulusVideoService videoService;

    @Override
    public void init() throws ServletException {
        videoService = new StimulusVideoService();
        logger.info("StimulusVideoController 初始化完成");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis(); // 记录请求开始时间
        logger.info("收到 GET 请求，参数：id=" + req.getParameter("id"));

        String idParam = req.getParameter("id");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                logger.info("根据 ID 获取刺激视频，ID：" + id);

                StimulusVideoDto videoDto = videoService.getVideoById(id);
                if (videoDto != null) {
                    out.write(objectMapper.writeValueAsString(videoDto));
                    logger.info("成功返回刺激视频，ID：" + id);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"未找到对应的视频\"}");
                    logger.warn("未找到对应 ID 的视频，ID：" + id);
                }
            } else {
                logger.info("获取所有刺激视频");
                List<StimulusVideoDto> videos = videoService.getAllVideos();
                out.write(objectMapper.writeValueAsString(videos));
                logger.info("成功返回所有刺激视频，共 " + videos.size() + " 条");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("处理 GET 请求时发生异常", e);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"处理请求时发生错误\"}");
            }
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("GET 请求处理完成，耗时：" + (endTime - startTime) + " ms");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("收到 POST 请求，参数：type=" + req.getParameter("type") + ", video_url=" + req.getParameter("video_url"));

        String type = req.getParameter("type");
        String url = req.getParameter("video_url");

        StimulusVideoDto videoDto = new StimulusVideoDto();
        videoDto.setTestType(type);
        videoDto.setVideoUrl(url);

        try {
            videoService.createVideo(videoDto);
            logger.info("成功存储刺激视频：" + videoDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"刺激视频存储成功\"}");
        } catch (Exception e) {
            logger.error("处理 POST 请求时发生异常", e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"刺激视频存储失败\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("收到 PUT 请求，参数：id=" + req.getParameter("id") + ", type=" + req.getParameter("type") + ", video_url=" + req.getParameter("video_url"));

        String id = req.getParameter("id");
        String type = req.getParameter("type");
        String url = req.getParameter("video_url");

        StimulusVideoDto videoDto = new StimulusVideoDto();
        videoDto.setVideoId(Long.valueOf(id));
        videoDto.setTestType(type);
        videoDto.setVideoUrl(url);

        try {
            videoService.updateVideo(videoDto);
            logger.info("成功更新刺激视频：" + videoDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"刺激视频更新成功\"}");
        } catch (Exception e) {
            logger.error("处理 PUT 请求时发生异常", e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"刺激视频更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("收到 DELETE 请求，参数：id=" + req.getParameter("id"));

        String IdParam = req.getParameter("id");
        Long Id = Long.valueOf(IdParam);

        try {
            videoService.deleteVideo(Id);
            logger.info("成功删除刺激视频，ID：" + Id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"刺激视频删除成功\"}");
        } catch (Exception e) {
            logger.error("处理 DELETE 请求时发生异常", e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"刺激视频删除失败\"}");
        }
    }
}
