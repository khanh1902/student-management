package com.springbot.tttn.domain.dtos.classes;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DeleteClassesDto {
    @Size(min = 1, message = "classIds at least 1 element")
    private List<Long> classIds;
}
