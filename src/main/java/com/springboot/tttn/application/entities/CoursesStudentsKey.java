package com.springbot.tttn.application.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoursesStudentsKey implements Serializable {
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "student_id")
    private String studentId;
}
