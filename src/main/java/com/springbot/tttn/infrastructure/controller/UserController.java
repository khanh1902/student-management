package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.dtos.users.ChangePasswordDto;
import com.springbot.tttn.domain.dtos.users.DeleteUserDTO;
import com.springbot.tttn.domain.dtos.users.UserDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/current")
    public ResponseEntity<Result> getCurrentUser(@RequestHeader(name = "Authorization", defaultValue = "") String jwt) {
        ResponseObject result = userService.getCurrentUser(jwt);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Result> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        ResponseObject result = userService.update(userDto, id);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @PutMapping("/change-password/{id}")
    public ResponseEntity<Result> updatePassword(@PathVariable UUID id, @RequestBody UserDto userDto) {
        ResponseObject result = userService.updatePassword(id, userDto.toUpdatePassword());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @GetMapping
    public ResponseEntity<Result> getStudentAccount(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    @RequestParam(name = "sortBy", required = false) String sortBy,
                                                    @RequestParam(name = "asc") boolean asc,
                                                    @RequestParam(name = "fullName", defaultValue = "") String fullName) {
        ResponseObject result = userService.getAllStudentAccount(pageNo, pageSize, sortBy, asc, fullName);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @DeleteMapping
    public ResponseEntity<Result> deletes(@Valid @RequestBody DeleteUserDTO ids) {
        ResponseObject result = userService.delete(ids.toUUIDs());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Result> BlockOrActiveUser(@RequestParam(name = "userId") UUID userId,
                                                    @RequestParam(name = "active") boolean active) {
        ResponseObject result = userService.blockUser(userId, active);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @PutMapping("/change-password")
    public ResponseEntity<Result> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
                                                 @RequestHeader(name = "Authorization", defaultValue = "") String jwt) {

        ResponseObject result = userService.changePassword(changePasswordDto, jwt);

        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
