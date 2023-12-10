package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbot.tttn.application.enums.EClassification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses_students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoursesStudents {
    @EmbeddedId
    private CoursesStudentsKey id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Courses courses;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "scores")
    private Double scores;

    @Column(name = "four-scores-scale")
    private Double fourScoresScale;

    @Column(name = "classification")
    private String classification;
}
