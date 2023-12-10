package com.springbot.tttn.domain.payloads.coursestudent;

import com.springbot.tttn.application.entities.LessonDay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CoursesOfStudentResponse {
    private Long courseId;
    private Long subjectId;
    private String subjectName;
    private Long credit;
    private Long classId;
    private String className;
    private String lesson;
    private List<LessonDay> lessonDays;
    private Date startDate;
    private Date endDate;

    public CoursesOfStudentResponse(Long courseId, Long subjectId, String subjectName, Long credit, Long classId, String className, String lesson, Date startDate, Date endDate) {
        this.courseId = courseId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.classId = classId;
        this.className = className;
        this.lesson = lesson;
        this.startDate = startDate;
        this.endDate = endDate;
        this.credit = credit;
    }
}
