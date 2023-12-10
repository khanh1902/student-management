package com.springbot.tttn.domain.dtos.classes;


import com.springbot.tttn.application.entities.Class;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassDto {
    @NotBlank(message = "Class name is required")
    private String className;

    @NotNull(message = "School year is required")
    private Long schoolYear;

    public Class toClass() {
        Class classroom = new Class();
        classroom.setClassName(this.className);
        classroom.setSchoolYear(this.schoolYear);
        return classroom;
    }
}
