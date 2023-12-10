package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.dtos.subjects.DeleteSubjectsDTO;
import com.springbot.tttn.domain.dtos.subjects.SubjectDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Result> getSubjects(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              @RequestParam(name = "sortBy", required = false) String sortBy,
                                              @RequestParam(name = "asc") boolean asc,
                                              @RequestParam(name = "subjectName", required = false) String subjectName) {
        ResponseObject result = subjectService.findAll(pageNo, pageSize, sortBy, subjectName, asc);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/select")
    public ResponseEntity<Result> selectAllSubject() {
        ResponseObject result = subjectService.getSubjectIdAndName();
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/register-for-student")
    public ResponseEntity<Result> selectAllSubjectRegister(@Valid @RequestParam String studentId) {
        ResponseObject result = subjectService.getSubjectRegisterForStudent(studentId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Result> addSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        ResponseObject result = subjectService.save(subjectDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Result> updateSubject(@Valid @RequestBody SubjectDTO subjectDTO, @PathVariable Long id) {
        ResponseObject result = subjectService.update(subjectDTO, id);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Result> deleteSubject(@Valid @RequestBody DeleteSubjectsDTO deleteSubjectsDTO) {
        ResponseObject result = subjectService.delete(deleteSubjectsDTO.toSubjectIds());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
