package com.springbot.tttn.domain.payloads.coursestudent;

import com.springbot.tttn.application.enums.EClassification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentOfCourseResponse {
    private String studentId;
    private String studentName;
    private Long classId;
    private String className;
    private Double scores;
    private Double fourScoresScale;
    private String classification;

}
