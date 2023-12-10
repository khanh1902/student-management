package com.springbot.tttn.infrastructure.repositories;

import com.springbot.tttn.application.entities.Schedule;
import com.springbot.tttn.domain.payloads.schedules.ScheduleResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Schedule s WHERE s.courses.courseId = :courseId", nativeQuery = false)
    void deleteByCourseId(@Param("courseId") Long courseId);

    @Transactional
    @Query(value = "SELECT s FROM Schedule s WHERE s.courses.courseId = :courseId AND s.lessonDay.lessonDayId = :lessonDayId ORDER BY s.dayLearn asc", nativeQuery = false)
    List<Schedule> findByCourseIdAndLessonDayId(@Param("courseId") Long courseId, @Param("lessonDayId") Long lessonDayId);

    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.schedules.ScheduleResponse(s.scheduleId,c.courseId,s.subject.subjectId,s.lesson.lessonId,c.classroom.classId,lesson.lessonName,c.classroom.className,s.subject.subjectName,s.dayLearn) FROM Schedule s INNER JOIN Lesson AS lesson ON lesson.lessonId = s.lesson.lessonId INNER JOIN Courses AS c ON  c.courseId=s.courses.courseId INNER JOIN LessonDay AS ld ON ld.lessonDayId=s.lessonDay.lessonDayId WHERE c.courseId = :courseId AND s.dayLearn = date(:dayLearn)", nativeQuery = false)
    List<ScheduleResponse> findAllScheduleByCourseIdAndDayLearn(@Param("courseId") Long courseId, @Param("dayLearn") String dayLearn);

    @Transactional
    @Query(value = "SELECT s FROM Schedule s WHERE s.dayLearn = date(:dayLearn) AND s.courses.courseId=:courseId ", nativeQuery = false)
    Schedule findByDayLearn(@Param("dayLearn") Date dayLearn, @Param("courseId") Long courseId);

    @Transactional
    @Query(value = "SELECT new com.springbot.tttn.domain.payloads.schedules.ScheduleResponse(s.scheduleId,c.courseId,s.subject.subjectId,s.lesson.lessonId,c.classroom.classId,lesson.lessonName,c.classroom.className,s.subject.subjectName,s.dayLearn)  FROM Schedule s INNER JOIN Lesson AS lesson ON lesson.lessonId = s.lesson.lessonId INNER JOIN Courses AS c ON  c.courseId=s.courses.courseId INNER JOIN LessonDay AS ld ON ld.lessonDayId=s.lessonDay.lessonDayId INNER JOIN CoursesStudents AS cs ON cs.courses.courseId=c.courseId WHERE cs.student.mssv=:studentId AND s.dayLearn = date(:dayLearn)", nativeQuery = false)
    List<ScheduleResponse> findAllScheduleByStudentIdAndDayLearn(@Param("studentId") String studentId, @Param("dayLearn") String dayLearn);

    @Transactional
    @Query(value = "SELECT COUNT(s.scheduleId) as num FROM Schedule s INNER JOIN CoursesStudents cs ON cs.courses.courseId = s.courses.courseId WHERE cs.student.mssv = :studentId AND date(s.dayLearn) BETWEEN date(:startDate) AND date(:endDate) GROUP BY s.scheduleId", nativeQuery = false)
    List<Long> numberDayLearnInWeek(
            @Param("studentId") String studentId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


}
