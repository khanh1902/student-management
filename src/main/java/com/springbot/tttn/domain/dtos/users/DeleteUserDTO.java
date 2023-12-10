package com.springbot.tttn.domain.dtos.users;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
public class DeleteUserDTO {

    @NotEmpty(message = "Ids is required")
    private List<UUID> ids;

    public List<UUID> toUUIDs() {
        return ids;
    }
}
