package com.student.service;

import com.student.domain.pojo.Student;
import com.student.domain.dto.StudentDTO;
import com.student.exception.BusinessException;
import com.student.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    // 查询所有学生信息(可带条件)
    public List<Student> list(String keyword) {
        return studentMapper.findAll(trimToNull(keyword));
    }

    // 根据学号ID查询学生信息
    public Student getById(String id) {
        String normalizedId = normalizeId(id);
        Student student = studentMapper.findById(normalizedId);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        return student;
    }

    // 添加学生信息
    @Transactional
    public Student create(StudentDTO studentDTO) {
        if (studentMapper.countById(studentDTO.getId().trim()) > 0) {
            throw new BusinessException("学号已存在");
        }
        checkEmailAvailable(studentDTO.getEmail(), null);
        Student student = toStudent(studentDTO);
        studentMapper.insert(student);
        return getById(student.getId());
    }

    // 修改学生信息
    @Transactional
    public Student update(String id, StudentDTO studentDTO) {
        String normalizedId = normalizeId(id);
        String requestedId = normalizeId(studentDTO.getId());

        if (studentMapper.findById(normalizedId) == null) {
            throw new BusinessException("学生不存在");
        }
        if (!normalizedId.equals(requestedId)) {
            throw new BusinessException("学号作为主键，修改时不能变更学号");
        }

        checkEmailAvailable(studentDTO.getEmail(), normalizedId);
        Student student = toStudent(studentDTO);
        student.setId(normalizedId);
        studentMapper.update(student);
        return getById(normalizedId);
    }

    // 删除学生信息
    @Transactional
    public void delete(String id) {
        if (studentMapper.deleteById(normalizeId(id)) == 0) {
            throw new BusinessException("学生不存在");
        }
    }

    // 检查邮箱是否重复
    private void checkEmailAvailable(String email, String excludeId) {
        if (studentMapper.countByEmail(email.trim(), excludeId) > 0) {
            throw new BusinessException("Email已存在");
        }
    }

    // 转换StudentDTO为Student实体
    private Student toStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId().trim());
        student.setName(studentDTO.getName().trim());
        student.setEmail(studentDTO.getEmail().trim());
        student.setGender(trimToNull(studentDTO.getGender()));
        student.setMajor(trimToNull(studentDTO.getMajor()));
        student.setGrade(studentDTO.getGrade());
        return student;
    }

    // 规范化普通字符串：移除字符串首尾空格，若为空则返回null
    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    // 规范化学号ID：移除首尾空格，若为空则抛出异常
    private String normalizeId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new BusinessException("学号不能为空");
        }
        return id.trim();
    }
}
