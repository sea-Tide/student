package com.student.controller;

import com.student.domain.dto.ChangePasswordDTO;
import com.student.domain.dto.Result;
import com.student.domain.dto.UserDTO;
import com.student.domain.dto.LoginDTO;
import com.student.domain.dto.RegisterDTO;
import com.student.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 登录
    @PostMapping("/login")
    public Result<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpSession session) {
        UserDTO user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        session.setAttribute("LOGIN_USER", user);
        return Result.ok(user);
    }

    // 注册
    @PostMapping("/register")
    public Result<UserDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return Result.ok(userService.register(registerDTO));
    }

    // 修改密码
    @PostMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
        return Result.ok();
    }

    // 获取当前登录用户信息
    @GetMapping("/me")
    public ResponseEntity<Result<UserDTO>> me(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.fail("请先登录"));
        }
        return ResponseEntity.ok(Result.ok(user));
    }

    // 注销
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.ok();
    }
}
