package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.User;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.application.utils.JwtUtils;
import com.springbot.tttn.domain.dtos.users.ChangePasswordDto;
import com.springbot.tttn.domain.dtos.users.UserDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.users.UserResponse;
import com.springbot.tttn.domain.services.UserService;
import com.springbot.tttn.infrastructure.repositories.StudentRepository;
import com.springbot.tttn.infrastructure.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ResponseObject update(UserDto userDto, UUID userId) {
        logger.info("Action: Update Student");
        User newUser = userDto.toUpdateUser();
        User isUser = userRepository.findByUserId(userId);
        if (isUser == null) {
            logger.info("Add new Student: " + newUser.toString());
            newUser.setUserId(UUID.randomUUID());
            return new ResponseObject(HttpStatus.CREATED, new Result("Save student successfully", userRepository.save(newUser)));
        }
        if (!newUser.getEmail().isEmpty()) isUser.setEmail(newUser.getEmail());
        if (!newUser.getFullName().isEmpty()) isUser.setFullName(newUser.getFullName());
        logger.info("Update Student from " + isUser + " to " + newUser);
        return new ResponseObject(HttpStatus.OK, new Result("Update student successfully", userRepository.save(isUser)));
    }

    @Override
    public ResponseObject delete(List<UUID> userIds) {
        logger.info("Action: Delete user");
        if (userIds.size() < 2) {
            User isUser = userRepository.findByUserId(userIds.get(0));
            logger.info("Delete user: " + isUser.toString());
            userRepository.deleteById(userIds.get(0));
            return new ResponseObject(HttpStatus.OK, new Result("Delete user successfully", null));
        }
        boolean isDelete = false;
        for (UUID userId : userIds) {
            User isUser = userRepository.findByUserId(userId);
            if (isUser == null) {
                logger.info("Error: User id" + userId + " does not exists to delete");
                continue;
            }
            isDelete = true;
            logger.info("Delete user: " + isUser);
            if (isUser.getStudent() != null) {
                studentRepository.deleteByMssv(isUser.getStudent().getMssv());
            }
            userRepository.deleteById(userId);
        }
        if (!isDelete) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Error: User does not exists to delete", null));
        }
        return new ResponseObject(HttpStatus.OK, new Result("Delete user successfully", null));
    }

    @Override
    public ResponseObject getCurrentUser(String jwt) {
        if (jwt.isEmpty()) {
            return new ResponseObject(HttpStatus.UNAUTHORIZED, new Result("Missing token", null));
        }
        String username = jwtUtils.getUserNameFromJwt(jwt);

        User userExist = userRepository.findByUserName(username);
        if (userExist == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("User does not exists", null));
        }
        return new ResponseObject(HttpStatus.OK, new Result("Get current use successfully", new UserResponse(userExist.getUserId(), userExist.getUsername(), userExist.getFullName(), userExist.getEmail(), userExist.getIsActive(), userExist.getRoles())));
    }

    @Override
    public ResponseObject updatePassword(UUID userId, String newPassword) {
        logger.info("Action: Update password");
        User isUser = userRepository.findByUserId(userId);
        if (isUser == null) {
            logger.info("Error: User " + userId + " does not exists to update password");
        }
        if (newPassword != null) isUser.setPassword(encoder.encode(newPassword));
        logger.info("Update password successfully");
        userRepository.save(isUser);
        return new ResponseObject(HttpStatus.OK, new Result("Update password successfully", null));
    }

    @Override
    public ResponseObject getAllStudentAccount(Integer pageNo, Integer pageSize, String sortBy, boolean asc, String fullName) {
        Pageable pageRequest = PageRequest.of(pageNo, pageSize);
        if (sortBy != null) {
            pageRequest = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        } else {
            sortBy = "username";
            pageRequest = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        }
        Page<User> findAll = userRepository.findAllByFullName(fullName, pageRequest);
        if (findAll.isEmpty()) {
            findAll = userRepository.findAll(pageRequest);
        }
        return new ResponseObject(HttpStatus.OK, new Result("Get student account successfully", Helper.PageToMap(findAll)));
    }

    @Override
    public ResponseObject blockUser(UUID userId, boolean isActive) {
        logger.info("Action: Block user");
        User isUser = userRepository.findByUserId(userId);
        if (isUser == null) {
            logger.info("Error: User id  " + userId + " not found");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("User not found", null));
        }
        isUser.setIsActive(isActive);
        userRepository.save(isUser);
        if (isActive) {
            logger.info("User id " + userId + " is active");
            return new ResponseObject(HttpStatus.OK, new Result("User id " + userId + " is active", null));
        }
        logger.info("User id " + userId + " is block");
        return new ResponseObject(HttpStatus.OK, new Result("User id " + userId + " is block", null));
    }

    @Override
    public ResponseObject changePassword(ChangePasswordDto changePasswordDto, String jwt) {
        if (jwt.isEmpty()) {
            return new ResponseObject(HttpStatus.UNAUTHORIZED, new Result("Missing token", null));
        }
        String username = jwtUtils.getUserNameFromJwt(jwt);

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Confirm password is not match", null));
        }

        User userExist = userRepository.findByUserName(username);

        if (userExist == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("User does not exists", null));
        }

        if (!encoder.matches(changePasswordDto.getOldPassword(), userExist.getPassword())) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Old password is not match", null));
        }

        userExist.setPassword(encoder.encode(changePasswordDto.getNewPassword()));

        userRepository.save(userExist);

        return new ResponseObject(HttpStatus.OK, new Result("Change password successfully", null));
    }
}
