/**
 * 这个类负责处理用户操作日志相关的 HTTP 请求并调用 UserActionLogService 来执行相应的业务逻辑。
 * 
 * @author 石振山
 * @version 2.3.1
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssvep.dto.UserActionLogDto;
import com.ssvep.service.UserActionLogService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/logs")
public class LogsController extends HttpServlet {
    private UserActionLogService logService;

    @Override
    public void init() throws ServletException {
        logService = new UserActionLogService();
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
                UserActionLogDto logDto = logService.getLogById(id);

                if (logDto != null) {
                    out.write(objectMapper.writeValueAsString(logDto));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"Log not found\"}");
                }
            } else if (userParam != null) {
                Long user_id = Long.valueOf(userParam);
                List<UserActionLogDto> logsDto = logService.getLogsByUser(user_id);
                out.write(objectMapper.writeValueAsString(logsDto));
            } else {
                List<UserActionLogDto> logsDto = logService.getAllLogs();
                out.write(objectMapper.writeValueAsString(logsDto));
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
        String userId = req.getParameter("user_id");
        String type = req.getParameter("type");
        String timestamp = req.getParameter("timestamp");

        UserActionLogDto logDto = new UserActionLogDto();
        logDto.setUserId(Long.valueOf(userId));
        logDto.setActionType(type);
        logDto.setTimestamp(LocalDateTime.parse(timestamp));

        try {
            logService.createLog(logDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"日志存储成功\"}");
        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"日志存储失败\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String userId = req.getParameter("user_id");
        String type = req.getParameter("type");
        String timestamp = req.getParameter("timestamp");

        UserActionLogDto logDto = new UserActionLogDto();
        logDto.setLogId(Long.valueOf(id));
        logDto.setUserId(Long.valueOf(userId));
        logDto.setActionType(type);
        logDto.setTimestamp(LocalDateTime.parse(timestamp));

        try {
            logService.updateLog(logDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"日志更新成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"日志更新失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String IdParam = req.getParameter("id");
        Long Id = Long.valueOf(IdParam);

        try {
            logService.deleteLog(Id);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"日志删除成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"日志删除失败\"}");
        }
    }

}
