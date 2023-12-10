package com.springbot.tttn.domain.services;

import com.springbot.tttn.domain.dtos.users.ChangePasswordDto;
import com.springbot.tttn.domain.dtos.users.UserDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.students.StudentReq;
import org.hibernate.type.AnyType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    ResponseObject update(UserDto userDto, UUID studentId);
    ResponseObject delete(List<UUID> userIds);
    ResponseObject getCurrentUser(String jwt);
    ResponseObject updatePassword(UUID userId, String newPassword);
    ResponseObject getAllStudentAccount(Integer pageNo, Integer pageSize, String sortBy, boolean asc, String fullName);
    ResponseObject blockUser(UUID userId, boolean isActive);

    ResponseObject changePassword(ChangePasswordDto changePasswordDto,String jwt    );

}
