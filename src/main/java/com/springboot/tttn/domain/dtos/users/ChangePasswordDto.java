package com.springbot.tttn.domain.dtos.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotBlank(message="Old password is required")
    private String oldPassword;

    @NotBlank(message="New password is required")
    private String newPassword;

    @NotBlank(message="Confirm password is required")
    private String confirmPassword;

}
