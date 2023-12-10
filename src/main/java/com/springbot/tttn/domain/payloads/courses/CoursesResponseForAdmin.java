package com.springbot.tttn.domain.payloads.courses;

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
@AllArgsConstructor
public class CoursesResponseForAdmin {
    private Long courseId;
    private Long subjectId;
    private Long classId;
    private String subjectName;
    private Long credit;
    private String lesson;
    private List<LessonDay> lessonDays;
    private Long maxStudents;
    private Date startDate;
    private Date endDate;
    private String className;
    private Long registered;
    private String status;

    public CoursesResponseForAdmin(Long courseId, Long subjectId,Long classId, String subjectName, Long credit, String lesson, Long maxStudents, Date startDate, Date endDate, String className, Long registered, String status) {
        this.courseId = courseId;
        this.subjectId = subjectId;
        this.classId = classId;
        this.subjectName = subjectName;
        this.lesson = lesson;
        this.maxStudents = maxStudents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.className = className;
        this.registered = registered;
        this.status = status;
        this.credit = credit;
    }
}

