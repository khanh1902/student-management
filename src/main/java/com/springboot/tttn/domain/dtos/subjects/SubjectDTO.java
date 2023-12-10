package com.springbot.tttn.domain.dtos.subjects;

import com.springbot.tttn.application.entities.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectDTO {
    @NotBlank(message = "Subject name is required")
    private String subjectName;

    @NotNull(message = "Credit is required")
    private Long credit;

    public Subject toSubject() {
        Subject subject = new Subject();
        subject.setSubjectName(subjectName);
        subject.setCredit(credit);
        return subject;
    }
}
