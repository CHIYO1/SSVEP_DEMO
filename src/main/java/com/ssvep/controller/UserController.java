package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssvep.dto.UserDto;
import com.ssvep.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/users")
public class UserController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        logger.info("用户管理控制器已初始化。");
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type,Accept,"
                + "content-Type,origin,x-requested-with,content-type,accept,authorization,token,id,X-Custom-Header,X-Cookie,Connection,User-Agent,Cookie,*");
        response.setHeader("Access-Control-Request-Headers",
                "Authorization,Origin, X-Requested-With,content-Type,Accept");
        response.setHeader("Access-Control-Expose-Headers", "*");
        logger.info("OPTIONS请求已处理，跨域头部已设置。");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();

            if (idParam != null) {
                Long id = Long.valueOf(idParam);
                UserDto userDto = userService.getUserById(id);

                if (userDto != null) {
                    out.write(objectMapper.writeValueAsString(userDto));
                    logger.info("成功获取ID为 {} 的用户信息。", id);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"未找到用户\"}");
                    logger.warn("未找到ID为 {} 的用户。", id);
                }
            } else {
                List<UserDto> users = userService.getAllUsers();
                out.write(objectMapper.writeValueAsString(users));
                logger.info("成功获取所有用户信息。");
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("请求处理时发生错误：", e);  // 使用日志记录异常
            try (PrintWriter out = resp.getWriter()) {
                out.write("{\"error\":\"处理请求时发生错误\"}");
                logger.error("获取用户信息时发生错误：", e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "*");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type,Accept,"
                + "content-Type,origin,x-requested-with,content-type,accept,authorization,token,id,X-Custom-Header,X-Cookie,Connection,User-Agent,Cookie,*");
        resp.setHeader("Access-Control-Request-Headers",
                "Authorization,Origin, X-Requested-With,content-Type,Accept");
        resp.setHeader("Access-Control-Expose-Headers", "*");

        BufferedReader reader = req.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String requestBody = jsonBuilder.toString();
        JSONObject json = new JSONObject(requestBody);

        // 从 JSON 数据中获取参数
        String action = json.optString("action", "");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if ("register".equalsIgnoreCase(action)) {
            String username = json.optString("username", "");
            String password = json.optString("password", "");
            String name = json.optString("name", "");
            String roleParam = "USER";
            UserDto.Role role = UserDto.Role.valueOf(roleParam.toUpperCase());

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setPassword(password);
            userDto.setName(name);
            userDto.setRole(role);

            try {
                userService.createUser(userDto);
                resp.getWriter().write("{\"status\":\"success\",\"message\":\"用户创建成功\"}");
                logger.info("用户 {} 创建成功。", username);
            } catch (Exception e) {
                logger.error("请求处理时发生错误：", e);  // 使用日志记录异常
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"创建用户失败\"}");
                logger.error("创建用户 {} 失败：", username, e);
            }

        } else if ("login".equalsIgnoreCase(action)) {
            // 处理登录逻辑
            String username = json.optString("username", "");
            String password = json.optString("password", "");

            try {
                boolean authenticated = userService.authenticate(username, password);

                if (authenticated) {
                    String jwtToken = userService.generateToken(username);
                    resp.getWriter()
                            .write("{\"status\":\"success\",\"message\":\"登录成功\",\"token\":\"" + jwtToken
                                    + "\"}");
                    logger.info("用户 {} 登录成功，生成JWT令牌。", username);
                } else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.getWriter().write("{\"status\":\"error\",\"message\":\"用户名或密码错误\"}");
                    logger.warn("用户 {} 登录失败，用户名或密码错误。", username);
                }

            } catch (Exception e) {
                logger.error("请求处理时发生错误：", e);  // 使用日志记录异常
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"登录失败\"}");
                logger.error("用户 {} 登录失败：", username, e);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdParam = req.getParameter("userId");
        Long userId = Long.valueOf(userIdParam);
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String roleParam = req.getParameter("role");
        UserDto.Role role = UserDto.Role.valueOf(roleParam.toUpperCase());

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setName(name);
        userDto.setRole(role);

        try {
            userService.updateUser(userDto);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"用户更新成功\"}");
            logger.info("用户ID为 {} 的信息更新成功。", userId);
        } catch (Exception e) {
            logger.error("请求处理时发生错误：", e);  // 使用日志记录异常
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"更新用户失败\"}");
            logger.error("更新用户ID为 {} 的信息失败：", userId, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userIdParam = req.getParameter("userId");
        Long userId = Long.valueOf(userIdParam);

        try {
            userService.deleteUser(userId);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"用户删除成功\"}");
            logger.info("用户ID为 {} 删除成功。", userId);
        } catch (Exception e) {
            logger.error("请求处理时发生错误：", e);  // 使用日志记录异常
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"删除用户失败\"}");
            logger.error("删除用户ID为 {} 失败：", userId, e);
        }
    }
}
