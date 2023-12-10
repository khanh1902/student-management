package com.springbot.tttn.domain.payloads.courses;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CourseIdRequest {
    @NotNull(message = "Course id is required")
    private Long courseId;
}
