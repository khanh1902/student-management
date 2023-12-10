package com.springbot.tttn.domain.dtos.courses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    @NotNull(message = "Subject is required")
    private Long subjectId;

    @NotNull(message = "Lesson is required")
    private Long lessonId;

    @NotNull(message = "Class is required")
    private Long classId;

    @NotEmpty(message = "Lesson day is required")
    private List<String> lessonDays;

    @NotBlank(message = "Start day is required")
    private String startDate;

    @NotNull(message = "Max students is required")
    private Long maxStudents;
}
