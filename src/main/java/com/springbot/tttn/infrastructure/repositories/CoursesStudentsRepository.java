package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.CoursesStudents;
import com.springbot.tttn.application.entities.CoursesStudentsKey;
import com.springbot.tttn.domain.payloads.coursestudent.CoursesOfStudentResponse;
import com.springbot.tttn.domain.payloads.coursestudent.ScoresOfStudentResponse;
import com.springbot.tttn.domain.payloads.coursestudent.StudentOfCourseResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesStudentsRepository extends JpaRepository<CoursesStudents, CoursesStudentsKey> {
    @Transactional
    @Query(value = "SELECT cs FROM CoursesStudents cs WHERE cs.courses.courseId = :courseId AND cs.student.mssv = :studentId", nativeQuery = false)
    CoursesStudents findByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") String studentId);

    @Transactional
    @Query(value = "SELECT cs FROM CoursesStudents cs WHERE cs.student.mssv = :studentId", nativeQuery = false)
    List<CoursesStudents> findByStudentId(@Param("studentId") String studentId);

    @Transactional
    @Query(value = "SELECT cs FROM CoursesStudents cs WHERE cs.id.courseId = :courseId", nativeQuery = false)
    List<CoursesStudents> findByCourseId(@Param("courseId") Long courseId);


    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.coursestudent.StudentOfCourseResponse(cs.student.mssv, cs.student.name,cs.student.classroom.classId, cs.student.classroom.className, cs.scores, cs.fourScoresScale, cs.classification) FROM CoursesStudents cs WHERE cs.courses.courseId = :courseId AND (:studentName IS NULL OR LOWER(cs.student.name)  LIKE LOWER(CONCAT('%', :studentName, '%'))) GROUP BY cs.student.mssv, cs.student.name, cs.student.classroom.className,cs.student.classroom.classId, cs.scores, cs.classification, cs.fourScoresScale", nativeQuery = false)
    Page<StudentOfCourseResponse> findStudentsRegisteredCourse(
            @Param("courseId") Long courseId,
            @Param("studentName") String studentName,
            Pageable pageable);

    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.coursestudent.CoursesOfStudentResponse(cs.courses.courseId, cs.courses.subject.subjectId, cs.courses.subject.subjectName, cs.courses.subject.credit, cs.courses.classroom.classId,cs.courses.classroom.className, cs.courses.lesson.lessonName, cs.courses.startDate, cs.courses.endDate) FROM CoursesStudents cs WHERE cs.student.mssv = :studentId GROUP BY cs.courses ORDER BY cs.courses.subject.subjectName asc", nativeQuery = false)
    Page<CoursesOfStudentResponse> findCoursesOfStudentRegistered(@Param("studentId") String studentId, Pageable pageable);

    @Transactional
    @Query(value = "SELECT COUNT(*) FROM CoursesStudents cs WHERE cs.id.courseId = :courseId", nativeQuery = false)
    Long countByCourseId(@Param("courseId") Long courseId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CoursesStudents cs WHERE cs.id.courseId = :courseId AND cs.id.studentId = :studentId", nativeQuery = false)
    void deleteByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") String studentId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CoursesStudents cs WHERE cs.id.courseId = :courseId", nativeQuery = false)
    void deleteByCourseId(@Param("courseId") Long courseId);

    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.coursestudent.ScoresOfStudentResponse(cs.student.mssv, cs.student.name, cs.student.classroom.classId, cs.student.classroom.className, cs.courses.courseId, cs.courses.subject.subjectId, cs.courses.subject.subjectName, cs.courses.subject.credit, cs.scores, cs.fourScoresScale, cs.classification) FROM CoursesStudents cs WHERE (:classId IS NULL OR cs.student.classroom.classId = :classId) AND (:studentName IS NULL OR LOWER(cs.student.name)  LIKE LOWER(CONCAT('%', :studentName, '%'))) GROUP BY cs.student.mssv, cs.student.name, cs.student.classroom.classId, cs.student.classroom.className, cs.courses.courseId, cs.courses.subject.subjectId, cs.courses.subject.subjectName, cs.courses.subject.credit, cs.scores, cs.classification, cs.fourScoresScale", nativeQuery = false)
    Page<ScoresOfStudentResponse> findScoresOfStudentByStudentIdAndOptionClassId(
            @Param("studentName") String studentName,
            @Param("classId") Long classId,
            Pageable pageable);

    @Transactional
    @Query(value = "SELECT SUM(s.credit) as credit FROM CoursesStudents cs INNER JOIN Courses c ON c.courseId=cs.courses.courseId INNER JOIN Subject s ON s.subjectId = c.subject.subjectId WHERE cs.student.mssv = :studentId GROUP BY cs.student.mssv", nativeQuery = false)
    Long numberCreditsOfStudent(@Param("studentId") String studentId);

    @Transactional
    @Query(value = "SELECT COUNT(cs) FROM CoursesStudents cs WHERE cs.student.mssv = :studentId GROUP BY cs.student.mssv", nativeQuery = false)
    Long numberCoursesOfStudent(@Param("studentId") String studentId);

    @Transactional
    @Query(value = "SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END FROM CoursesStudents cs WHERE cs.id.courseId = :courseId AND cs.scores IS NULL", nativeQuery = false)
    Boolean isNullScoreOfCourse(@Param("courseId") Long courseId);
}
