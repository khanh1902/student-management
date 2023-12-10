package com.springbot.tttn.domain.services;

import com.springbot.tttn.application.entities.User;
import com.springbot.tttn.domain.dtos.users.UserDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.users.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseObject login (LoginRequest loginRequest);
    ResponseObject register(UserDto userDto);
}
