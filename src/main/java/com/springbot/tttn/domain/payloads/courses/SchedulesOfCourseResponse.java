package com.springbot.tttn.domain.payloads.courses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesOfCourseResponse {
    private String schedule;
    private String date;
}
