package com.springbot.tttn.domain.payloads.students;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentYetRegisterResponse {
    private String studentId;
    private String studentName;
}
