package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.Class;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.springbot.tttn.domain.payloads.classes.ResponseClass;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    @Transactional
    @Query(value = "SELECT c FROM Class c WHERE c.className = :className", nativeQuery = false)
    Class findByClassName(@Param("className") String className);

    @Query(value = "SELECT c FROM Class c WHERE c.classId = :classId", nativeQuery = false)
    Class findByClassId(@Param("classId") Long classId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Class c WHERE c.classId = :classId", nativeQuery = false)
    void deleteByClassId(@Param("classId") Long classId);

    @Query(value = "SELECT NEW com.springbot.tttn.domain.payloads.classes.ResponseClass(c.classId, c.className, c.schoolYear, COUNT(s.mssv)) FROM Class c LEFT JOIN Student s ON s.classroom.classId = c.classId WHERE (:className IS NULL OR c.className LIKE :className) GROUP BY c.classId, c.className, c.schoolYear", nativeQuery = false)
    Page<ResponseClass> getAllClasses(@Param("className") String className, Pageable pageable);
}