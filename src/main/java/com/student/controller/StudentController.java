package com.student.controller;

import com.student.domain.dto.Result;
import com.student.domain.pojo.Student;
import com.student.domain.dto.StudentDTO;
import com.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // 查询学生列表(可带条件)
    @GetMapping
    public Result<List<Student>> list(@RequestParam(required = false) String keyword) {
        return Result.ok(studentService.list(keyword));
    }

    // 查询学生详情
    @GetMapping("/{id}")
    public Result<Student> getById(@PathVariable String id) {
        return Result.ok(studentService.getById(id));
    }

    // 添加学生信息
    @PostMapping
    public Result<Student> create(@Valid @RequestBody StudentDTO request) {
        return Result.ok(studentService.create(request));
    }

    // 修改学生信息
    @PutMapping("/{id}")
    public Result<Student> update(@PathVariable String id, @Valid @RequestBody StudentDTO request) {
        return Result.ok(studentService.update(id, request));
    }

    // 删除学生信息
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        studentService.delete(id);
        return Result.ok();
    }
}
