package com.springbot.tttn.domain.dtos.notifications;

import com.springbot.tttn.application.entities.Notification;
import com.springbot.tttn.application.enums.ENotificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationDto {
    @NotNull(message = "title is required")
    private String title;

    @NotNull(message = "content is required")
    private String content;

    @NotNull(message = "status is required")
    private ENotificationStatus status;


   public Notification toNotification(){
        Notification notification = new Notification();
        notification.setTitle(this.title);
        notification.setContent(this.content);
        notification.setStatus(this.status);
        return notification;
    }
}
