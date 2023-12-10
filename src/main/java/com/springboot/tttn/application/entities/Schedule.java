package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Courses courses;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_day_id")
    private LessonDay lessonDay;

    @Column(name = "day_learn")
    private Date dayLearn;

    public Schedule(Courses courses, Subject subject, Lesson lesson, Date dayLearn, LessonDay lessonDay) {
        this.courses = courses;
        this.subject = subject;
        this.lesson = lesson;
        this.dayLearn = dayLearn;
        this.lessonDay = lessonDay;
    }

    @Override
    public String toString() {
        return "{" +
                ", course=" + courses.getCourseId() +
                ", subject=" + subject.getSubjectName() +
                '}';
    }
}
