package com.springbot.tttn.domain.dtos.courses;

import com.springbot.tttn.application.entities.CoursesStudents;
import com.springbot.tttn.application.entities.CoursesStudentsKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelRegisteredCoursesDTO {
    @NotNull(message = "course is required")
    private Long courseId;

    @NotBlank(message = "Student is required")
    private String studentId;

    public CoursesStudents toCoursesStudents() {
        CoursesStudents coursesStudents = new CoursesStudents();
        coursesStudents.setId(new CoursesStudentsKey(this.courseId, this.studentId));
        return coursesStudents;
    }
}
