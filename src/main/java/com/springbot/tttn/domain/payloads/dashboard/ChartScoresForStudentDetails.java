package com.springbot.tttn.domain.payloads.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartScoresForStudentDetails {
    private Long subjectId;
    private String subjectName;
    private Double averageScores;
    private Double studentScores;
}
