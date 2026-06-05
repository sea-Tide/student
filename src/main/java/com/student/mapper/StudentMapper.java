package com.student.mapper;

import com.student.domain.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMapper {
    // 查询所有学生信息(可带条件)
    List<Student> findAll(@Param("keyword") String keyword);

    // 根据学号ID查询学生信息
    Student findById(@Param("id") String id);

    // 判断学号是否重复
    int countById(@Param("id") String id);

    // 统计指定邮箱的学生数量，可排除当前学号ID
    int countByEmail(@Param("email") String email, @Param("excludeId") String excludeId);

    // 添加学生信息
    int insert(Student student);

    // 修改学生信息
    int update(Student student);

    // 删除学生信息
    int deleteById(@Param("id") String id);
}
