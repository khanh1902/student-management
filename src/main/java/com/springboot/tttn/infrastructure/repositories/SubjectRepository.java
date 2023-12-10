package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.Subject;
import com.springbot.tttn.domain.payloads.subjects.SubjectIdCreditAndNameResponse;
import com.springbot.tttn.domain.payloads.subjects.SubjectResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Transactional
    @Query(value = "SELECT NEW com.springbot.tttn.domain.payloads.subjects.SubjectResponse(s.subjectId, s.subjectName, s.credit, s.isDelete) FROM Subject s WHERE :subjectName IS NULL OR LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :subjectName, '%')) GROUP BY s.subjectId, s.subjectName, s.credit, s.isDelete", nativeQuery = false)
    Page<SubjectResponse> findAllBySubjectName(@Param("subjectName") String subjectName, Pageable pageable);

    @Transactional
    @Query(value = "SELECT s FROM Subject s WHERE s.subjectName = :subjectName", nativeQuery = false)
    Subject findBySubjectName(@Param("subjectName") String subjectName);

    @Transactional
    @Query(value = "SELECT s FROM Subject s WHERE s.subjectId = :subjectId", nativeQuery = false)
    Subject findBySubjectId(@Param("subjectId") Long subjectId);

    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.subjects.SubjectIdCreditAndNameResponse(s.subjectId, s.subjectName,s.credit) FROM Subject s", nativeQuery = false)
    List<SubjectIdCreditAndNameResponse> getIdAndName();

    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.subjects.SubjectIdCreditAndNameResponse(s.subjectId, s.subjectName,s.credit) FROM Subject s INNER JOIN Courses c ON(c.subject.subjectId = s.subjectId) WHERE NOT EXISTS (SELECT cs FROM CoursesStudents cs WHERE cs.courses.subject.subjectId = s.subjectId AND cs.student.mssv = :studentId) GROUP BY s.subjectName, s.subjectId", nativeQuery = false)
    List<SubjectIdCreditAndNameResponse> getForRegister(@Param("studentId") String studentId);
}
