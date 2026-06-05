package com.student.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession(false) == null ? null : request.getSession(false).getAttribute("LOGIN_USER"); // getSession(false)表示若没有登录状态，不创建新的空会话
        if (loginUser != null) {
            return true; // 已登录，放行
        }
        if (request.getRequestURI().startsWith("/api/")) { // 如果是以api开头的接口请求，不予使用，返回未登录JSON信息
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置401响应状态码
            response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 设置UTF8编码
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置为application/json响应格式
            response.getWriter().write("{\"code\": 401, \"message\": \"未登录！\", \"data\": null}"); // 返回JSON信息
        } else { // 如果是页面请求，重定向到登录页
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
        return false;
    }
}
