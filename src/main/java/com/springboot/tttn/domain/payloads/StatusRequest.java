package com.springbot.tttn.domain.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class StatusRequest {
    @NotBlank(message = "Status is required")
    private String status;
}
