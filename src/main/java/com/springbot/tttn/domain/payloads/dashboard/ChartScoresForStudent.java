package com.springbot.tttn.domain.payloads.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChartScoresForStudent {
    private Double cumulativeAverageScores;
    private List<ChartScoresForStudentDetails> chartDetails;
}
