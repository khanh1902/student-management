package com.springbot.tttn.domain.services;

import com.springbot.tttn.domain.dtos.courses.*;
import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CoursesService {
    ResponseObject getCoursesBySubjectIdForStudent(String studentId, Long subjectId);
    ResponseObject getCoursesForAdmin(Integer pageNo,
                                      Integer pageSize,
                                      String sortBy,
                                      String subjectName,
                                      boolean asc);
    ResponseObject getStudentOfClassYetRegisterCourse(Long courseId, Long classId);
    ResponseObject create(CourseDTO courseDTO);
    ResponseObject updateStatus(Long courseId, String status);
    ResponseObject deleteCourse(List<Long> courseIds);
    ResponseObject registerCourseByStudent(RegisterCoursesDTO registerCoursesDTO);
    ResponseObject registerSubjectForStudents(RegisterCourseForStudentsDTO registerCourseForStudentsDTO);
    ResponseObject registerCourseByAdmin(RegisterCoursesDTO registerCoursesDTO);
    ResponseObject cancelCourseRegistered(CancelRegisteredCoursesDTO cancelRegisteredCoursesDTO);
    ResponseObject getCourseById(Long courseId);
}
