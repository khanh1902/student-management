package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.Notification;
import com.springbot.tttn.application.enums.ENotificationStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Transactional
    @Query(value = "SELECT n FROM Notification n WHERE (:title IS NULL OR n.title LIKE :title) AND (:status IS NULL OR n.status = :status)", nativeQuery = false)
    Page<Notification> getAllNotifications(
            @Param("title") String title,
            @Param("status") ENotificationStatus status,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Notification n WHERE n.id IN :ids", nativeQuery = false)
    void deleteNotifications(@Param("ids") List<UUID> ids);

    @Transactional
    @Query(value = "SELECT n FROM Notification n WHERE n.id in :ids", nativeQuery = false)
    List<Notification> getNotificationsByIds(@Param("ids") List<UUID> ids);

    @Transactional
    Notification findBySlug(String slug);

    @Transactional
    @Query(value = "SELECT COUNT(n) FROM Notification n WHERE n.slug = :slug", nativeQuery = false)
    int countBySlug(@Param("slug") String slug);

    @Transactional
    @Query(value = "SELECT n FROM Notification n WHERE n.id = :id", nativeQuery = false)
    Notification findById(@Param("id") UUID id);

}
