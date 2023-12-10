package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.LessonDay;
import com.springbot.tttn.application.enums.ELessonDay;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonDayRepository extends JpaRepository<LessonDay, Long> {
    @Transactional
    @Query(value = "SELECT ld FROM LessonDay ld", nativeQuery = false)
    List<LessonDay> findAll();

    @Transactional
    @Query(value = "SELECT ld FROM LessonDay ld WHERE ld.day = :day", nativeQuery = false)
    LessonDay findByDay(@Param("day") ELessonDay day);
}
