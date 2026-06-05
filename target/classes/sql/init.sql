CREATE DATABASE IF NOT EXISTS student_system
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE student_system;

DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  username VARCHAR(50) NOT NULL COMMENT 'login username',
  password VARCHAR(100) NOT NULL COMMENT 'login password',
  display_name VARCHAR(80) NOT NULL COMMENT 'display name',
  role VARCHAR(30) NOT NULL DEFAULT 'ADMIN' COMMENT 'user role',
  enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'enabled flag',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updated time',
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='system users';

CREATE TABLE students (
  id VARCHAR(32) NOT NULL COMMENT 'student number / primary key',
  name VARCHAR(40) NOT NULL COMMENT 'student name',
  email VARCHAR(120) NOT NULL COMMENT 'email',
  gender VARCHAR(10) NULL COMMENT 'gender',
  major VARCHAR(80) NULL COMMENT 'major',
  grade INT NULL COMMENT 'entry year',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updated time',
  PRIMARY KEY (id),
  UNIQUE KEY uk_students_email (email),
  KEY idx_students_name (name),
  KEY idx_students_major (major)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='student information';

INSERT INTO users (username, password, display_name, role, enabled)
VALUES
  ('admin', '123', '系统管理员', 'ADMIN', 1);

INSERT INTO students (id, name, email, gender, major, grade)
VALUES
  ('2026001', '张三', 'zhangsan@example.com', '男', '计算机科学与技术', 2026),
  ('2026002', '李四', 'lisi@example.com', '女', '软件工程', 2026),
  ('2026003', '王五', 'wangwu@example.com', '男', '数据科学与大数据技术', 2025);
