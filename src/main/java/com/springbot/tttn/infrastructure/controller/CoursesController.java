package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.dtos.courses.*;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.StatusRequest;
import com.springbot.tttn.domain.services.CoursesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CoursesController {
    @Autowired
    private CoursesService coursesService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/students")
    public ResponseEntity<Result> getCoursesBySubjectId(@RequestParam(name = "studentId") String studentId,
                                                        @RequestParam(name = "subjectId") Long subjectId) {
        ResponseObject result = coursesService.getCoursesBySubjectIdForStudent(studentId, subjectId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students-yet-register")
    public ResponseEntity<Result> getStudentOfClassYetRegister(@Valid @RequestParam(name = "courseId") Long courseId,
                                                               @Valid @RequestParam(name = "classId") Long classId) {
        ResponseObject result = coursesService.getStudentOfClassYetRegisterCourse(courseId, classId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<Result> getCourseForAdmin(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     @RequestParam(name = "sortBy", required = false) String sortBy,
                                                     @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                                                     @RequestParam(name = "subjectName", required = false) String subjectName) {
        ResponseObject result = coursesService.getCoursesForAdmin(pageNo, pageSize, sortBy, subjectName, asc);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/update-status")
    public ResponseEntity<Result> updateStatusCourse(@PathVariable Long id, @RequestBody StatusRequest status) {
        ResponseObject result = coursesService.updateStatus(id, status.getStatus());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Result> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        ResponseObject result = coursesService.create(courseDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Result> deleteCourses(@Valid @RequestBody DeleteCoursesDTO deleteCoursesDTO) {
        ResponseObject result = coursesService.deleteCourse(deleteCoursesDTO.getCourseIds());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @PostMapping("/register/by-student")
    public ResponseEntity<Result> registerCourseByStudent(@Valid @RequestBody RegisterCoursesDTO registerCoursesDTO) {
        ResponseObject result = coursesService.registerCourseByStudent(registerCoursesDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/by-admin-students")
    public ResponseEntity<Result> registerCourseForStudentsByAdmin(@Valid @RequestBody RegisterCourseForStudentsDTO registerCourseForStudentsDTO) {
        ResponseObject result = coursesService.registerSubjectForStudents(registerCourseForStudentsDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/by-admin")
    public ResponseEntity<Result> registerCourseByAdmin(@Valid @RequestBody RegisterCoursesDTO registerCoursesDTO) {
        ResponseObject result = coursesService.registerCourseByAdmin(registerCoursesDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @DeleteMapping("/cancel-registered")
    public ResponseEntity<Result> cancelRegisteredCourse(@Valid @RequestBody CancelRegisteredCoursesDTO cancelRegisteredCoursesDTO) {
        ResponseObject result = coursesService.cancelCourseRegistered(cancelRegisteredCoursesDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/{courseId}")
    public ResponseEntity<Result> getCourseById(@PathVariable Long courseId) {
        ResponseObject result = coursesService.getCourseById(courseId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
