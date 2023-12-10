package com.springbot.tttn.domain.dtos.coursesstudents;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseStudentDTO {
    @NotNull(message = "Course id is required")
    private Long courseId;

    @NotBlank(message = "Student id is required")
    private String studentId;

    @NotNull(message = "Scores is required")
    private Double scores;
}