package com.springbot.tttn.domain.payloads.students;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class StudentIdRequest {
    @NotBlank(message = "Student is required")
    private String studentId;
}
