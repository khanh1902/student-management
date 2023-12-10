package com.springbot.tttn.domain.services;

import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {

    ResponseObject getStudentDashboard(String studentId);
    ResponseObject getAllScoresForStudent(String studentId);

}
