package com.springbot.tttn.domain.payloads.dashboard;

import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.application.entities.Student;
import com.springbot.tttn.domain.payloads.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class StudentDashboard {

    private Student student;
    private Class classroom;
    private Long totalCourse;
    private Long numberDayLearnInWeek;
    private Long numCredits;
    private Double cumulativeAverageScores;
    private List<ChartScoresForStudentDetails> chartScoresForStudent;
}
