package com.springbot.tttn.domain.payloads.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListScoresForStudentDetail {
    List<ListScoresForStudent> listScores;
    private Long totalCreditPassed;
    private Long totalCreditFailed;
    private Double averageScores;
    private Double averageScaleFourScores;
    private String classificationDetails;
}
