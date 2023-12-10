package com.springbot.tttn.domain.dtos.students;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DeleteStudentsDto {
    @Size(min = 1, message = "Mssvs at least 1 element")
    private List<String> mssvs;
}
