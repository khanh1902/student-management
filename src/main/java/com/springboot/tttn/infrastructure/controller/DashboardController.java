package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Result> getDashboardStudent(@PathVariable("studentId") String studentId) {
        ResponseObject result = dashboardService.getStudentDashboard(studentId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/student/learning-outcomes/{studentId}")
    public ResponseEntity<Result> LearningOutComes(@PathVariable("studentId") String studentId) {
        ResponseObject result = dashboardService.getAllScoresForStudent(studentId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
