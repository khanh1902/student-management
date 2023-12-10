package com.springbot.tttn.domain.payloads.subjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {
    private Long subjectId;
    private String subjectName;
    private Long credit;
    private Boolean isDelete;
}