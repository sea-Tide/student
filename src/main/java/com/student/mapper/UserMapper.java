package com.student.mapper;

import com.student.domain.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User findByUsername(@Param("username") String username);

    int insert(User user);

    int updatePassword(@Param("username") String username, @Param("password") String password);
}
