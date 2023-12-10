package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.dtos.coursesstudents.CourseStudentDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.CoursesStudentsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses/students")
public class CourseStudentController {
    @Autowired
    private CoursesStudentsService coursesStudentsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students-of-course")
    public ResponseEntity<Result> getStudentRegisteredCourse(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                                                             @RequestParam(name = "sortBy", required = false) String sortBy,
                                                             @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                                                             @RequestParam(name = "courseId") Long courseId,
                                                             @RequestParam(name = "studentName", required = false) String studentName) {
        ResponseObject result = coursesStudentsService.getStudentRegisteredCourse(pageNo, pageSize, sortBy, asc, courseId, studentName);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/courses-of-student")
    public ResponseEntity<Result> getCoursesOfStudentRegistered(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                                                                @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                                                                @RequestParam(name = "studentId") String studentId) {
        ResponseObject result = coursesStudentsService.getCoursesOfStudentRegistered(pageNo, pageSize, sortBy, asc, studentId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-scores")
    public ResponseEntity<Result> updateScoresForStudent(@Valid @RequestBody CourseStudentDTO courseStudentDTO) {
        ResponseObject result = coursesStudentsService.updateScores(courseStudentDTO.getStudentId(), courseStudentDTO.getCourseId(), courseStudentDTO.getScores());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/scores")
    public ResponseEntity<Result> getScoresOfStudent(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                                                     @RequestParam(name = "sortBy", required = false) String sortBy,
                                                     @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                                                     @RequestParam(name = "studentName", required = false) String studentName,
                                                     @RequestParam(name = "classId", required = false) Long classId) {
        ResponseObject result = coursesStudentsService.getScoresByClassAndStudentName(pageNo, pageSize, sortBy, asc, classId, studentName);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }


}