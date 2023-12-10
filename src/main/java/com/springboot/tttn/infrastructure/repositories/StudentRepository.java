package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.Student;
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
public interface StudentRepository extends JpaRepository<Student, String> {
    @Transactional
    @Query(value = "SELECT st FROM Student st WHERE st.mssv = :mssv", nativeQuery = false)
    Student findStudentByMssv(@Param("mssv") String mssv);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Student s WHERE s.mssv IN :mssvs", nativeQuery = false)
    void deleteStudentsByMssv(@Param("mssvs") List<String> mssvs);

    @Modifying
    @Transactional
    @Query(value = "SELECT s FROM Student s WHERE s.mssv IN :mssvs", nativeQuery = false)
    List<Student> getManyStudentByMssv(@Param("mssvs") List<String> mssvs);

    @Transactional
    List<Student> getStudentsByClassroom_ClassId(Long classId);

    @Transactional
    @Query(value = "SELECT NEW com.springbot.tttn.domain.payloads.students.ResponseStudent(s.mssv,s.name,s.address,c.className,c.classId,c.schoolYear)  FROM Student s INNER JOIN s.classroom c WHERE (:name IS NULL OR s.name LIKE :name) AND (:className IS NULL OR c.className LIKE :className)", nativeQuery = false)
    Page<Student> getAllStudents(
            @Param("name") String name,
            @Param("className") String className,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query (value = "DELETE FROM Student st WHERE st.mssv = :mssv", nativeQuery = false)
    void deleteByMssv(@Param("mssv") String mssv);
}
