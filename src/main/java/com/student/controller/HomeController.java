package com.student.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session) {
        Object loginUser = session.getAttribute("LOGIN_USER");
        return loginUser == null ? "redirect:/login.html" : "redirect:/students.html";
    }
}
