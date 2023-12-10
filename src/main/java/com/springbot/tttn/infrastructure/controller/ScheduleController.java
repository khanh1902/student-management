package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.dtos.schedules.CreateScheduleDTO;
import com.springbot.tttn.domain.dtos.schedules.UpdateScheduleDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;


@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> getAllSchedulesByCourseId(@PathVariable Long courseId, @RequestParam Date startDate) {
        ResponseObject result = scheduleService.getAllSchedulesByCourseId(courseId, startDate);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<Result> getAllSchedulesByStudentId(@PathVariable String studentId, @RequestParam Date startDate) {
        ResponseObject result = scheduleService.getAllSchedulesByStudentId(studentId, startDate);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @DeleteMapping("/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> deleteSchedule(@PathVariable Long scheduleId) {
        ResponseObject result = scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> updateSchedule( @Valid @RequestBody  UpdateScheduleDTO updateScheduleDTO) {
        ResponseObject result = scheduleService.updateSchedule(updateScheduleDTO);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Result> createSchedule(@Valid @RequestBody  CreateScheduleDTO createScheduleDTO) {
        ResponseObject resutl = scheduleService.createSchedule(createScheduleDTO);

        return ResponseEntity.status(resutl.getStatusCode()).body(resutl.getResult());
    }

}
