package com.springbot.tttn.domain.services;

import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

@Service
public interface CoursesStudentsService {
    ResponseObject getStudentRegisteredCourse(Integer pageNo, Integer pageSize, String sortBy, Boolean asc, Long courseId, String studentName);
    ResponseObject getCoursesOfStudentRegistered(Integer pageNo, Integer pageSize, String sortBy, Boolean asc, String studentId);
    ResponseObject updateScores(String studentId, Long courseId, Double scores);
    ResponseObject getScoresByClassAndStudentName(Integer pageNo, Integer pageSize, String sortBy, Boolean asc, Long classId, String studentName);
}
