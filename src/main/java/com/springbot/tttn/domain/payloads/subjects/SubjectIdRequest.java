package com.springbot.tttn.domain.payloads.subjects;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SubjectIdRequest {
    @NotNull(message = "Subject id is required")
    private Long subjectId;
}
