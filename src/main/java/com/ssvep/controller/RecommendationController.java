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
    private RecommendationService recommendationService;

    @Override
    public void init() throws ServletException {
        recommendationService = new RecommendationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String user = req.getParameter("user_id");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                TreatmentRecommendationDto recommendationDto = recommendationService.getrecommendationById(id);

                if (recommendationDto != null) {
                    out.write(objectMapper.writeValueAsString(recommendationDto));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Recommendation not found\"}");
                }

            } else if (user != null) {
                Long user_id = Long.valueOf(user);
                List<TreatmentRecommendationDto> recommendationDto = recommendationService
                        .getrecommendationsByUser(user_id);
                out.write(objectMapper.writeValueAsString(recommendationDto));

            } else {
                List<TreatmentRecommendationDto> recommendations = recommendationService.getAllRecommendation();
                out.write(objectMapper.writeValueAsString(recommendations));

            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"An error occurred while processing the request\"}");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"治疗建议添加成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"治疗建议添加失败\"}");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"治疗建议更新成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"治疗建议更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String IdParam = req.getParameter("id");
        Long Id = Long.valueOf(IdParam);

        try {
            recommendationService.deleteRecommendation(Id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"治疗建议删除成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"治疗建议删除失败\"}");
        }
    }

}
