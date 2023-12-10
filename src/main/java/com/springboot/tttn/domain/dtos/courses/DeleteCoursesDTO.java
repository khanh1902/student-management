package com.springbot.tttn.domain.dtos.courses;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class DeleteCoursesDTO {
    @Size(min = 1, message = "Mssvs at least 1 element")
    private List<Long> courseIds;
}
