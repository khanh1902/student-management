package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.Lesson;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Transactional
    @Query(value = "SELECT s FROM Lesson s", nativeQuery = false)
    List<Lesson> findAll();

    @Transactional
    @Query(value = "SELECT s FROM Lesson s WHERE s.lessonId = :lessonId", nativeQuery = false)
    Lesson findByScheduleId(@Param("lessonId") Long lessonId);
}
