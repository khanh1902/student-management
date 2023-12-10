package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.Role;
import com.springbot.tttn.application.entities.User;
import com.springbot.tttn.application.enums.ERole;
import com.springbot.tttn.application.security.UserDetailsImpl;
import com.springbot.tttn.application.utils.JwtUtils;
import com.springbot.tttn.domain.dtos.users.UserDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.users.LoginRequest;
import com.springbot.tttn.domain.payloads.users.UserResponse;
import com.springbot.tttn.domain.services.AuthService;
import com.springbot.tttn.domain.services.UserService;
import com.springbot.tttn.infrastructure.repositories.RoleRepository;
import com.springbot.tttn.infrastructure.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    private Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public ResponseObject login(LoginRequest loginRequest) {
        logger.info("Action: User Login");
        User checkUser = loginRequest.toUser();

        User user = userRepository.findByUserName(checkUser.getUsername());

        if (user == null) {
            logger.info("User Name " + loginRequest.getUsername() + " not found!");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("User not found", null));
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.info("Password Does Not Match!");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Password does not match", null));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        Map<String, String> token = new HashMap<>();
        token.put("token", jwt);
        logger.info("User " + user.getUsername() + " login successfully!");
        return new ResponseObject(HttpStatus.OK, new Result("Login successfully", token));
    }

    @Override
    public ResponseObject register(UserDto userDto) {
        logger.info("Action: Register Admin");
        User user = userDto.toUser();
        User isUsername = userRepository.findByUserName(user.getUsername());
        if(isUsername != null) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("User already exists", null));
        }

        User isEmail = userRepository.findByEmail(user.getEmail());
        if(isEmail != null) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Email already exists", null));
        }

        Set<Role> setRole = new HashSet<>();
        Role roleExists = roleRepository.findByName(ERole.ROLE_ADMIN);
        setRole.add(roleExists != null ? roleExists : roleRepository.save(new Role(ERole.ROLE_ADMIN)));

        User adminUser = new User(
                UUID.randomUUID(),
                user.getUsername(),
                user.getFullName(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                setRole
        );
        User newUser = userRepository.save(adminUser);
        logger.info("Register successfully!");
        return new ResponseObject(HttpStatus.OK, new Result("Register successfully", new UserResponse(
                newUser.getUserId(),
                newUser.getUsername(),
                newUser.getFullName(),
                newUser.getEmail(),
                newUser.getIsActive(),
                newUser.getRoles())
        ));
    }
}
