package com.springbot.tttn.domain.dtos.users;

import com.springbot.tttn.application.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank (message = "Full Name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    private String email;


    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setFullName(this.fullName);
        user.setEmail(this.email);
        return user;
    }

    public User toUpdateUser() {
        User user = new User();
        user.setFullName(this.fullName);
        user.setEmail(this.email);
        return user;
    }

    public String toUpdatePassword() {
        User user = new User();
        user.setPassword(this.password);
        return user.getPassword();
    }

}
