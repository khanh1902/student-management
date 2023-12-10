package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.application.enums.ENotificationStatus;
import com.springbot.tttn.domain.dtos.notifications.DeleteNotificationsDto;
import com.springbot.tttn.domain.dtos.notifications.NotificationDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")

    public ResponseEntity<Result> getAllNotifications(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      @RequestParam(name = "sortBy", required = false) String sortBy,
                                                      @RequestParam(name = "title", required = false) String title,
                                                      @RequestParam(name = "status", required = false) ENotificationStatus status,
                                                      @RequestParam(name = "asc", defaultValue = "true", required = false) boolean asc) {
        ResponseObject result = notificationService.getAllNotification(pageNo, pageSize, sortBy, title, status, asc);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Result> createNotification(@Valid @RequestBody NotificationDto notificationDto) {
        ResponseObject result = notificationService.createNotification(notificationDto);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @GetMapping("/slug/{slug}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<Result> getNotificationBySlug(@PathVariable("slug") String slug) {
        ResponseObject result = notificationService.findNotificationBySlug(slug);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")

    public ResponseEntity<Result> getNotificationById(@PathVariable("id") String id) {
        ResponseObject result = notificationService.getNotificationById(id);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Result> updateNotification(@PathVariable("id") String id, @Valid @RequestBody NotificationDto notificationDto) {
        ResponseObject result = notificationService.updateNotification(id, notificationDto);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Result> deleteNotification(@PathVariable("id") String id) {
        ResponseObject result = notificationService.deleteNotification(id);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Result> deleteNotifications(@Valid @RequestBody DeleteNotificationsDto deleteNotificationsDto) {
        ResponseObject result = notificationService.deleteNotifications(deleteNotificationsDto.getIds());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
