/**
 * 这个类负责处理检测记录相关的 HTTP 请求并调用 TestRecordService 来执行相应的业务逻辑。
 *
 * @author 石振山
 * @version 4.3.2
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.TestRecordDto;
import com.ssvep.service.TestRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class TestRecordController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(TestRecordController.class);
    private TestRecordService recordService;

    @Override
    public void init() throws ServletException {
        logger.info("初始化 TestRecordController...");
        recordService = new TestRecordService();
        logger.info("TestRecordController 初始化完成。");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String userParam = req.getParameter("user_id");

        logger.info("收到 GET 请求。id: {}, user_id: {}", idParam, userParam);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                logger.info("查询指定 ID 的记录: {}", id);
                TestRecordDto recordDto = recordService.getRecordById(id);

                if (recordDto != null) {
                    out.write(objectMapper.writeValueAsString(recordDto));
                    logger.info("查询成功，返回记录: {}", recordDto);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Record not found\"}");
                    logger.warn("未找到 ID 为 {} 的记录。", id);
                }
            } else if (userParam != null) {
                Long userId = Long.valueOf(userParam);
                logger.info("查询指定用户的所有记录，用户 ID: {}", userId);
                List<TestRecordDto> recordDto = recordService.getRecordsByUser(userId);
                out.write(objectMapper.writeValueAsString(recordDto));
                logger.info("查询成功，返回记录数量: {}", recordDto.size());
            } else {
                logger.info("查询所有记录...");
                List<TestRecordDto> recordDto = recordService.getAllRecords();
                out.write(objectMapper.writeValueAsString(recordDto));
                logger.info("查询成功，返回记录数量: {}", recordDto.size());
            }

        } catch (Exception e) {
            logger.error("处理 GET 请求时发生异常: ", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"An error occurred while processing the request\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("收到 POST 请求，开始处理...");
        String userId = req.getParameter("user_id");
        String testType = req.getParameter("test_type");
        String testDate = req.getParameter("test_date");
        String testResults = req.getParameter("test_results");
        String relatedInfo = req.getParameter("related_info");
        String stimulusVideoId = req.getParameter("video_id");

        logger.debug("解析 POST 请求参数: user_id={}, test_type={}, test_date={}, video_id={}", userId, testType, testDate, stimulusVideoId);

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
            logger.info("开始创建检测记录...");
            recordService.createRecord(recordDto);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"检测记录存储成功\"}");
            logger.info("检测记录存储成功: {}", recordDto);

        } catch (Exception e) {
            logger.error("创建检测记录时发生异常: ", e);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"检测记录存储失败\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("收到 PUT 请求，开始处理...");
        String id = req.getParameter("id");
        String userId = req.getParameter("user_id");
        String testType = req.getParameter("test_type");
        String testDate = req.getParameter("test_date");
        String testResults = req.getParameter("test_results");
        String relatedInfo = req.getParameter("related_info");
        String stimulusVideoId = req.getParameter("video_id");

        logger.debug("解析 PUT 请求参数: id={}, user_id={}, test_type={}, test_date={}, video_id={}", id, userId, testType, testDate, stimulusVideoId);

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
            logger.info("开始更新检测记录...");
            recordService.updateRecord(recordDto);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"检测记录更新成功\"}");
            logger.info("检测记录更新成功: {}", recordDto);

        } catch (Exception e) {
            logger.error("更新检测记录时发生异常: ", e);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"检测记录更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("收到 DELETE 请求，开始处理...");
        String IdParam = req.getParameter("id");
        Long Id = Long.valueOf(IdParam);

        logger.debug("解析 DELETE 请求参数: id={}", Id);

        try {
            logger.info("开始删除检测记录...");
            recordService.deleteRecord(Id);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"检测记录删除成功\"}");
            logger.info("检测记录删除成功，ID: {}", Id);

        } catch (Exception e) {
            logger.error("删除检测记录时发生异常: ", e);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"检测记录删除失败\"}");
        }
    }
}
