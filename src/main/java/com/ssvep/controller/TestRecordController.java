/**
 * 这个类负责处理检测记录相关的 HTTP 请求并调用 TestRecordService 来执行相应的业务逻辑。
 * 
 * @author 石振山
 * @version 4.3.1
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.TestRecordDto;
import com.ssvep.service.TestRecordService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/testrecords")
public class TestRecordController extends HttpServlet{
    private TestRecordService recordService;

    @Override
    public void init() throws ServletException {
        recordService = new TestRecordService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String userParam = req.getParameter("user_id");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                TestRecordDto recordDto = recordService.getRecordById(id);

                if (recordDto != null) {
                    out.write(objectMapper.writeValueAsString(recordDto));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Record not found\"}");
                }
            } else if (userParam != null) {
                Long user_id = Long.valueOf(userParam);
                List<TestRecordDto> recordDto = recordService.getRecordsByUser(user_id);
                out.write(objectMapper.writeValueAsString(recordDto));
            } else {
                List<TestRecordDto> recordDto = recordService.getAllRecords();
                out.write(objectMapper.writeValueAsString(recordDto));
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
        String userId = req.getParameter("user_id");
        String testType = req.getParameter("test_type");
        String testDate = req.getParameter("test_date");
        String testResults = req.getParameter("test_results");
        String relatedInfo = req.getParameter("related_info");
        String stimulusVideoId = req.getParameter("video_id");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> ResultsMap = objectMapper.readValue(testResults, Map.class);

        TestRecordDto recordDto = new TestRecordDto();
        recordDto.setUserId(Long.valueOf(userId));
        recordDto.setTestType(testType);
        recordDto.setTestDate(LocalDate.parse(testDate));
        recordDto.setTestResults(ResultsMap);
        recordDto.setRelatedInfo(relatedInfo);
        recordDto.setStimulusVideoId(Long.valueOf(stimulusVideoId));

        try {
            recordService.createRecord(recordDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"检测记录存储成功\"}");
        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"检测记录存储失败\"}");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String userId = req.getParameter("user_id");
        String testType = req.getParameter("test_type");
        String testDate = req.getParameter("test_date");
        String testResults = req.getParameter("test_results");
        String relatedInfo = req.getParameter("related_info");
        String stimulusVideoId = req.getParameter("video_id");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> ResultsMap = objectMapper.readValue(testResults, Map.class);

        TestRecordDto recordDto = new TestRecordDto();
        recordDto.setRecordId(Long.valueOf(id));
        recordDto.setUserId(Long.valueOf(userId));
        recordDto.setTestType(testType);
        recordDto.setTestDate(LocalDate.parse(testDate));
        recordDto.setTestResults(ResultsMap);
        recordDto.setRelatedInfo(relatedInfo);
        recordDto.setStimulusVideoId(Long.valueOf(stimulusVideoId));

        try {
            recordService.updateRecord(recordDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"检测记录更新成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"检测记录更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String IdParam = req.getParameter("id");
        Long Id = Long.valueOf(IdParam);

        try {
            recordService.deleteRecord(Id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"检测记录删除成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"检测记录删除失败\"}");
        }
    }

}
