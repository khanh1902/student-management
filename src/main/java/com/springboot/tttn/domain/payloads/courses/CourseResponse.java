package com.springbot.tttn.domain.payloads.courses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseResponse {
    private String courseId;
    private String subjectName;
    private String registered;
    private String status;
    private boolean isRecommend;
    private List<SchedulesOfCourseResponse> schedulesOfCourse;
}
