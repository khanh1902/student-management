package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Transactional
    @Query(value = "SELECT u FROM User u WHERE u.userId = :userId", nativeQuery = false)
    User findByUserId(@Param("userId") UUID userId);

    @Transactional
    @Query(value = "SELECT NEW com.springbot.tttn.domain.payloads.students.StudentAccountResponse(u.userId, u.username, u.fullName, u.email, s.classroom.className, u.isActive) FROM User u INNER JOIN Student s ON (s.mssv = u.username) WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))", nativeQuery = false)
    Page<User> findAllByFullName(@Param("fullName") String fullName, Pageable pageable);

    @Transactional
    @Query(value = "SELECT NEW com.springbot.tttn.domain.payloads.students.StudentAccountResponse(u.userId, u.username, u.fullName, u.email, s.classroom.className, u.isActive) FROM User u INNER JOIN Student s ON (s.mssv = u.username)", nativeQuery = false)
    Page<User> findAll(Pageable pageable);

    @Transactional
    @Query(value = "SELECT u FROM User u WHERE u.username = :username", nativeQuery = false)
    User findByUserName(@Param("username") String username);

    @Transactional
    @Query(value = "SELECT u FROM User u WHERE u.email = :email", nativeQuery = false)
    User findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM User u WHERE u.userId = :userId", nativeQuery = false)
    void deleteById(@Param("userId") UUID userId);
}
