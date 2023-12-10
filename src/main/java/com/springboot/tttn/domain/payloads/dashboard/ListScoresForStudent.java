package com.springbot.tttn.domain.payloads.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListScoresForStudent {
    private Long courseId;
    private Long subjectId;
    private String subjectName;
    private Long credit;
    private Double scores;
    private Double scaleFourScores;
    private String classification;
    private String classificationDetails;
}
