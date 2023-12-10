package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.dtos.students.DeleteStudentsDto;
import com.springbot.tttn.domain.dtos.students.StudentDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Result> addStudent(@Valid @RequestBody StudentDto studentDto) {
        ResponseObject result = studentService.createStudent(studentDto);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/{mssv}")
    public ResponseEntity<Result> getStudentByMssv(@PathVariable String mssv) {
        ResponseObject result = studentService.findStudentByMssv(mssv);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @PutMapping("/{mssv}")
    public ResponseEntity<Result> updateStudent(@Valid @RequestBody StudentDto studentDto, @PathVariable String mssv) {
        ResponseObject result = studentService.updateStudent(studentDto, mssv);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Result> deleteStudents(@Valid @RequestBody DeleteStudentsDto deleteStudentsDto) {
        ResponseObject result = studentService.deleteStudents(deleteStudentsDto.getMssvs());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Result> getAllStudents(@RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(required = false) String sortBy,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) boolean asc,
                                                 @RequestParam(required = false) String className) {
        ResponseObject result = studentService.findAll(pageNo, pageSize, sortBy, name, asc, className);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
