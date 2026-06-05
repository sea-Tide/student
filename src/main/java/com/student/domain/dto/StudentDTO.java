package com.student.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentDTO {

    @NotBlank(message = "学号不能为空")
    @Size(max = 32, message = "学号长度不能超过32个字符")
    private String id;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 40, message = "姓名长度不能超过40个字符")
    private String name;

    @NotBlank(message = "Email不能为空")
    @Email(message = "Email格式不正确")
    @Size(max = 120, message = "Email长度不能超过120个字符")
    private String email;

    @Size(max = 10, message = "性别长度不能超过10个字符")
    private String gender;

    @Size(max = 80, message = "专业长度不能超过80个字符")
    private String major;

    @Min(value = 2000, message = "入学年份不能早于2000年")
    @Max(value = 2100, message = "入学年份不能晚于2100年")
    private Integer grade;
}
