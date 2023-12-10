package com.springbot.tttn.domain.dtos.courses;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class RegisterCourseForStudentsDTO {
    @NotNull(message = "Course id is required")
    private Long courseId;

    @NotEmpty(message = "Students is required")
    private List<String> students;
}
