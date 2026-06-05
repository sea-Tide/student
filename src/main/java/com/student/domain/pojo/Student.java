package com.student.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String id;
    private String name;
    private String email;
    private String gender;
    private String major;
    private Integer grade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
