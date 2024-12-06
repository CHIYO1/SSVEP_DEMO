/**
 * 这个类负责处理治疗建议相关的 HTTP 请求并调用 RecommendationService 来执行相应的业务逻辑。
 * 
 * @author 石振山
 * @version 2.3.1
 */
/**
 * 这个类负责处理治疗建议相关的 HTTP 请求并调用 RecommendationService 来执行相应的业务逻辑。
 *
 * @author 石振山
 * @version 2.3.1
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssvep.dto.TreatmentRecommendationDto;
import com.ssvep.service.RecommendationService;
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
import java.util.Map;

@WebServlet("/recommendations")
public class RecommendationController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(StimulusVideoService.class);
    private RecommendationService recommendationService;

    @Override
    public void init() throws ServletException {
        recommendationService = new RecommendationService();
        logger.info("RecommendationController 初始化完成"); // 初始化日志
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        logger.info("处理 GET 请求，参数: id=" + req.getParameter("id") + ", user_id=" + req.getParameter("user_id"));

        String idParam = req.getParameter("id");
        String user = req.getParameter("user_id");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                logger.info("根据 ID 获取治疗建议: " + id);
                TreatmentRecommendationDto recommendationDto = recommendationService.getrecommendationById(id);

                if (recommendationDto != null) {
                    out.write(objectMapper.writeValueAsString(recommendationDto));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"未找到治疗建议\"}");
                    logger.warn("未找到对应 ID 的治疗建议: " + id);
                }

            } else if (user != null) {
                Long user_id = Long.valueOf(user);
                logger.info("根据用户 ID 获取治疗建议: " + user_id);
                List<TreatmentRecommendationDto> recommendationDto = recommendationService
                        .getrecommendationsByUser(user_id);
                out.write(objectMapper.writeValueAsString(recommendationDto));

            } else {
                logger.info("获取所有治疗建议");
                List<TreatmentRecommendationDto> recommendations = recommendationService.getAllRecommendation();
                out.write(objectMapper.writeValueAsString(recommendations));

            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("处理 GET 请求时发生异常", e);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"处理请求时发生错误\"}");
            }
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("GET 请求处理完成，耗时: " + (endTime - startTime) + " ms");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("处理 POST 请求，参数: user_id=" + req.getParameter("user_id"));
        String id = req.getParameter("id");
        String userId = req.getParameter("user_id");
        String advice = req.getParameter("advice");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> adviceMap = objectMapper.readValue(advice, Map.class);

        TreatmentRecommendationDto recommendationDto = new TreatmentRecommendationDto();
        recommendationDto.setRecommendationId(Long.valueOf(id));
        recommendationDto.setUserId(Long.valueOf(userId));
        recommendationDto.setAdvice(adviceMap);

        try {
            recommendationService.createrecommendation(recommendationDto);
            logger.info("成功添加治疗建议: " + recommendationDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"治疗建议添加成功\"}");

        } catch (Exception e) {
            logger.error("处理 POST 请求时发生异常", e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"治疗建议添加失败\"}");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("处理 PUT 请求，参数: id=" + req.getParameter("id"));
        String id = req.getParameter("id");
        String userId = req.getParameter("user_id");
        String advice = req.getParameter("advice");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> adviceMap = objectMapper.readValue(advice, Map.class);

        TreatmentRecommendationDto recommendationDto = new TreatmentRecommendationDto();
        recommendationDto.setRecommendationId(Long.valueOf(id));
        recommendationDto.setUserId(Long.valueOf(userId));
        recommendationDto.setAdvice(adviceMap);

        try {
            recommendationService.updateRecommendation(recommendationDto);
            logger.info("成功更新治疗建议: " + recommendationDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"治疗建议更新成功\"}");

        } catch (Exception e) {
            logger.error("处理 PUT 请求时发生异常", e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"治疗建议更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("处理 DELETE 请求，参数: id=" + req.getParameter("id"));
        String IdParam = req.getParameter("id");
        Long Id = Long.valueOf(IdParam);

        try {
            recommendationService.deleteRecommendation(Id);
            logger.info("成功删除治疗建议，ID: " + Id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"治疗建议删除成功\"}");

        } catch (Exception e) {
            logger.error("处理 DELETE 请求时发生异常", e);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"治疗建议删除失败\"}");
        }
    }

}

