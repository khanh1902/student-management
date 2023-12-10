package com.springbot.tttn.domain.dtos.schedules;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CreateScheduleDTO {
    @NotNull(message = "dayLearn is required")
    Date dayLearn;
    @NotNull(message = "lessonId is required")
    Long lessonId;

    @NotEmpty(message = "lessonDayId is required")
    String lessonDay;

    @NotNull(message = "courseId is required")
    Long courseId;

    @NotNull(message ="subjectId is required")
    Long subjectId;
}
