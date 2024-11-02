/**
 * 这个类负责处理用户相关的 HTTP 请求并调用 UserService 来执行相应的业务逻辑。
 * 
 * @author 石振山
 * @version 2.3.1
 */
package com.ssvep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssvep.dto.UserDto;
import com.ssvep.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/users")
public class UserController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
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
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"error\":\"User not found\"}");
                }
            } else {
                List<UserDto> users = userService.getAllUsers();
                out.write(objectMapper.writeValueAsString(users));
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
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String roleParam = req.getParameter("role");
        UserDto.Role role = UserDto.Role.valueOf(roleParam.toUpperCase());

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setName(name);
        userDto.setRole(role);

        try {
            userService.createUser(userDto);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"用户创建成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"创建用户失败\"}");
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

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"更新用户失败\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdParam = req.getParameter("userId");
        Long userId = Long.valueOf(userIdParam);

        try {
            userService.deleteUser(userId);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"success\",\"message\":\"用户删除成功\"}");

        } catch (Exception e) {
            e.printStackTrace();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"删除用户失败\"}");
        }
    }

}
