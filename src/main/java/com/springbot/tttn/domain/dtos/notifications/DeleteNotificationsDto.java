package com.springbot.tttn.domain.dtos.notifications;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeleteNotificationsDto {
    @Size(min = 1, message = "ids at least 1 element")
    private List<UUID> ids;

}
