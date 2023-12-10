package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.Notification;
import com.springbot.tttn.application.enums.ENotificationStatus;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.domain.dtos.notifications.NotificationDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.NotificationService;
import com.springbot.tttn.infrastructure.repositories.NotificationRepository;
import com.springbot.tttn.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public ResponseObject getAllNotification(Integer pageNo, Integer pageSize, String sortBy, String title, ENotificationStatus status, boolean asc) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());

        if (sortBy != null) {
            pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        }

        title = title == null ? null : "%" + title + "%";

        Page<Notification> page = notificationRepository.getAllNotifications(title, status, pageable);

        return new ResponseObject(HttpStatus.OK, new Result("Get all classes successfully", Helper.PageToMap(page)));
    }

    @Override
    public ResponseObject getNotificationById(String id) {
        Notification notification = notificationRepository.findById((UUID.fromString(id)));

        if (notification == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Notification not found", null));
        }

        return new ResponseObject(HttpStatus.OK, new Result("Get notification successfully", notification));
    }

    @Override
    public ResponseObject createNotification(NotificationDto notificationDto) {
        Notification notification = notificationDto.toNotification();

        String slug = Helper.toSlug(notification.getTitle());

        int count = notificationRepository.countBySlug(slug);

        notification.setSlug(count > 0 ? slug + "-" + count : slug);

        Notification createNotification = notificationRepository.save(notification);

        return new ResponseObject(HttpStatus.CREATED, new Result("Create notification successfully", createNotification));
    }

    @Override
    public ResponseObject updateNotification(String id, NotificationDto notificationDto) {
        ResponseObject notificationResponse = this.getNotificationById(id);

        if (notificationResponse.getStatusCode() != HttpStatus.OK) {
            return notificationResponse;
        }

        int count = notificationRepository.countBySlug(Helper.toSlug(notificationDto.getTitle()));

        Notification notification = (Notification) notificationResponse.getResult().getData();

        String slug = Helper.toSlug(notificationDto.getTitle());

        notification.setTitle(notificationDto.getTitle());
        notification.setContent(notificationDto.getContent());
        notification.setStatus(notificationDto.getStatus());
        notification.setSlug(count > 0 ? slug + "-" + count : slug);


        Notification updateNotification = notificationRepository.save(notification);

        return new ResponseObject(HttpStatus.OK, new Result("Update notification successfully", updateNotification));
    }

    @Override
    public ResponseObject deleteNotification(String id) {
        ResponseObject notificationResponse = this.getNotificationById(id);

        if (notificationResponse.getStatusCode() != HttpStatus.OK) {
            return notificationResponse;
        }

        notificationRepository.deleteById(id);

        return new ResponseObject(HttpStatus.OK, new Result("Delete notification successfully", null));
    }

    @Override
    public ResponseObject deleteNotifications(List<UUID> ids) {
        List<Notification> notifications = notificationRepository.getNotificationsByIds(ids);

        if (notifications.size() != ids.size()) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Some notification not exist", null));
        }

        notificationRepository.deleteNotifications(ids);

        return new ResponseObject(HttpStatus.OK, new Result("Delete notifications successfully", null));
    }

    @Override
    public ResponseObject findNotificationBySlug(String slug) {
        Notification notification = notificationRepository.findBySlug(slug);

        if (notification == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Notification not found", null));
        }

        return new ResponseObject(HttpStatus.OK, new Result("Get notification successfully", notification));
    }

}
