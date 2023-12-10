package com.springbot.tttn.domain.dtos.students;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class StudentYetRegisterCourseDTO {
    private Long classId;

    @NotNull(message = "Course id is requied")
    private Long courseId;
}
