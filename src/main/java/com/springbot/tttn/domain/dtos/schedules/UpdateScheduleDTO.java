package com.springbot.tttn.domain.dtos.schedules;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Getter
@NoArgsConstructor
public class UpdateScheduleDTO {
    @NotNull(message = "scheduleId is required")
    Long scheduleId;
    @NotNull(message = "dayLearn is required")
    Date dayLearn;
    @NotNull(message = "lessonId is required")
    Long lessonId;

    @NotEmpty(message = "lessonDayId is required")
    String lessonDay;
}
