package com.student.service;

import com.student.domain.dto.ChangePasswordDTO;
import com.student.domain.dto.RegisterDTO;
import com.student.domain.pojo.User;
import com.student.domain.dto.UserDTO;
import com.student.exception.BusinessException;
import com.student.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserDTO login(String username, String password) {
        String normalizedUsername = username == null ? "" : username.trim(); // 规范化字符串
        if (!StringUtils.hasText(normalizedUsername) || !StringUtils.hasText(password)) { // 非空校验
            throw new BusinessException("账号或密码错误");
        }

        User user = userMapper.findByUsername(normalizedUsername);
        if (user == null || !Boolean.TRUE.equals(user.getEnabled()) || !password.equals(user.getPassword())) { // 校验用户是否启用，且密码是否匹配
            throw new BusinessException("账号或密码错误");
        }

        return toUserDTO(user);
    }

    @Transactional
    public UserDTO register(RegisterDTO registerDTO) {
        String username = normalizeText(registerDTO.getUsername(), "账号不能为空");
        String password = normalizeText(registerDTO.getPassword(), "密码不能为空");
        String confirmPassword = normalizeText(registerDTO.getConfirmPassword(), "确认密码不能为空");
        String displayName = normalizeText(registerDTO.getDisplayName(), "显示名称不能为空");

        if (!password.equals(confirmPassword)) {
            throw new BusinessException("两次输入的密码不一致");
        }
        if (userMapper.findByUsername(username) != null) {
            throw new BusinessException("账号已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setDisplayName(displayName);
        user.setRole("USER");
        user.setEnabled(true);
        userMapper.insert(user);
        return toUserDTO(user);
    }

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        String username = normalizeText(changePasswordDTO.getUsername(), "账号不能为空");
        String oldPassword = normalizeText(changePasswordDTO.getOldPassword(), "原密码不能为空");
        String newPassword = normalizeText(changePasswordDTO.getNewPassword(), "新密码不能为空");
        String confirmPassword = normalizeText(changePasswordDTO.getConfirmPassword(), "确认密码不能为空");

        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException("两次输入的新密码不一致");
        }

        User user = userMapper.findByUsername(username);
        if (user == null || !Boolean.TRUE.equals(user.getEnabled()) || !oldPassword.equals(user.getPassword())) {
            throw new BusinessException("账号或原密码错误");
        }

        userMapper.updatePassword(username, newPassword);
    }

    private UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setDisplayName(user.getDisplayName());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private String normalizeText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(message);
        }
        return value.trim();
    }
}
