package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.Courses;
import com.springbot.tttn.application.entities.Schedule;
import com.springbot.tttn.domain.payloads.courses.CoursesResponseForAdmin;
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
public interface CoursesRepository extends JpaRepository<Courses, Long> {
    @Transactional
    @Query(value = "SELECT c FROM Courses c WHERE c.subject.subjectId = :subjectId AND c.lesson.lessonId = :lessonId", nativeQuery = false)
    List<Courses> findBySubjectIdAndLessonId(@Param("subjectId") Long subjectId, @Param("lessonId") Long lessonId);

    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.courses.CoursesResponseForAdmin(c.courseId, c.subject.subjectId, c.classroom.classId,c.subject.subjectName, c.subject.credit, c.lesson.lessonName, c.maxStudents, c.startDate, c.endDate, c.classroom.className, COUNT(cs), c.status) FROM Courses c LEFT JOIN CoursesStudents cs ON(cs.courses.courseId = c.courseId) WHERE :subjectName IS NULL OR LOWER(c.subject.subjectName) LIKE LOWER(CONCAT('%', :subjectName, '%')) GROUP BY c.courseId, c.subject.subjectId, c.subject.subjectName, c.lesson.lessonName, c.maxStudents, c.startDate, c.endDate, c.classroom.className", nativeQuery = false)
    Page<CoursesResponseForAdmin> findAllBySubjectName(@Param("subjectName") String subjectName, Pageable pageable);

    @Transactional
    @Query(value = "SELECT c FROM Courses c WHERE c.lesson.lessonId = :lessonId", nativeQuery = false)
    List<Courses> findByLessonId(@Param("lessonId") Long lessonId);

    @Transactional
    @Query(value = "SELECT c FROM Courses c WHERE c.subject.subjectId = :subjectId", nativeQuery = false)
    List<Courses> findBySubjectId(@Param("subjectId") Long subjectId);

    @Transactional
    @Query(value = "SELECT c FROM Courses c WHERE c.courseId = :courseId", nativeQuery = false)
    Courses findByCourseId(@Param("courseId") Long courseId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Courses c WHERE c.courseId = :courseId", nativeQuery = false)
    void deleteByCourseId(@Param("courseId") Long courseId);

    @Transactional
    @Query(value = "SELECT c FROM Courses c INNER JOIN Subject s ON s.subjectId = c.subject.subjectId INNER JOIN Class cl ON cl.className=c.classroom WHERE c.courseId=:courseId", nativeQuery = false)
    Courses findCoursesDetail(@Param("courseId") Long courseId);

    @Query(value = "SELECT c FROM Courses c WHERE c.subject.subjectId = :subjectId AND c.classroom.classId = :classId", nativeQuery = false)
    Courses findBySubjectIdAndClassId(@Param("subjectId") Long subjectId, @Param("classId") Long classId);


    @Transactional
    @Query(value = "SELECT c FROM Courses c where c.classroom.classId=:classId", nativeQuery = false)
    List<Courses> findAllByClassId(@Param("classId") Long classId);
}
