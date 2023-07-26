package org.caesar.finalWork.controller;

import org.caesar.finalWork.domain.entity.User;
import org.caesar.finalWork.domain.vo.Response;
import org.caesar.finalWork.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {

    private UserServiceImpl userService = UserServiceImpl.getInstance();

    //登录
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = (String) req.getParameter("username");
        String password = (String) req.getParameter("password");
        System.out.println(username + " " + password);
        User user = null;
        try {
            user = userService.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> map = new HashMap<>();
        if(Objects.isNull(user)){
            resp.getWriter().write(Response.ERROR(map, "用户名或密码错误").toJSONString());
            resp.getWriter().close();
            return;
        }
        map.put("user", user);
        resp.getWriter().write(Response.OK(map).toJSONString());
        resp.getWriter().close();
    }

    //注册
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
