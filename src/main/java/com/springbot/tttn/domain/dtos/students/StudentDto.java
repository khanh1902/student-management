package com.springbot.tttn.domain.dtos.students;

import com.springbot.tttn.application.entities.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDto {
    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private String className;

    public Student toStudent() {
        Student student = new Student();
        student.setName(this.name);
        student.setAddress(this.address);
        return student;
    }
}
