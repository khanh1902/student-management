package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbot.tttn.application.constants.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @JsonIgnore
    @OneToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<CoursesStudents> coursesStudents;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Class classroom;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "courses_lesson_day", joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_day_id"))
    private List<LessonDay> lessonDays;

    @JsonIgnore
    @OneToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "max_students")
    private Long maxStudents;

    @Column(name = "status")
    private String status;

    public Courses(Subject subject, Class classroom, Lesson lesson, List<LessonDay> lessonDays, Date startDate, Date endDate, Long maxStudents) {
        this.subject = subject;
        this.classroom = classroom;
        this.lesson = lesson;
        this.lessonDays = lessonDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
        this.status = Constants.PENDING;
    }

    @Override
    public String toString() {
        return "[" +
                "courseId=" + courseId +
                ", subject=" + subject +
                ", classroom=" + classroom +
                ", schedules=" + lesson +
                ", lessonDay=" + lessonDays.toString() +
                ", startDate=" + startDate +
                ']';
    }
}
