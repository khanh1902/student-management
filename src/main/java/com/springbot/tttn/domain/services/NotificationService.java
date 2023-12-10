package com.springbot.tttn.domain.services;

import com.springbot.tttn.application.entities.Notification;
import com.springbot.tttn.application.enums.ENotificationStatus;
import com.springbot.tttn.domain.dtos.notifications.DeleteNotificationsDto;
import com.springbot.tttn.domain.dtos.notifications.NotificationDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface NotificationService {
    ResponseObject getAllNotification(Integer pageNo, Integer pageSize, String sortBy, String title,
                                      ENotificationStatus status,
                                      boolean asc);

    ResponseObject getNotificationById(String id);

    ResponseObject createNotification(
            NotificationDto notificationDto
    );

    ResponseObject updateNotification(
            String id,
            NotificationDto notificationDto
    );

    ResponseObject deleteNotification(String id);

    ResponseObject deleteNotifications(
            List<UUID> ids
    );

    ResponseObject findNotificationBySlug(String slug);


}
