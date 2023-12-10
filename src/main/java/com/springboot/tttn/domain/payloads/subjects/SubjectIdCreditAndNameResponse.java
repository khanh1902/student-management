package com.springbot.tttn.domain.payloads.subjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectIdCreditAndNameResponse {
    private Long subjectId;
    private String subjectName;
    private Long credit;
}
