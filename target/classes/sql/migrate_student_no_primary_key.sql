USE student_system;

-- Move old student_no values into id, then remove the student_no column.
ALTER TABLE students MODIFY id BIGINT NOT NULL;
ALTER TABLE students DROP PRIMARY KEY;
ALTER TABLE students MODIFY id VARCHAR(32) NOT NULL COMMENT 'student number / primary key';
UPDATE students SET id = student_no;
ALTER TABLE students DROP INDEX uk_students_student_no;
ALTER TABLE students ADD PRIMARY KEY (id);
ALTER TABLE students DROP COLUMN student_no;
