package com.springbot.tttn.domain.services;

import com.springbot.tttn.domain.dtos.schedules.CreateScheduleDTO;
import com.springbot.tttn.domain.dtos.schedules.UpdateScheduleDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface ScheduleService {

    ResponseObject getAllSchedulesByCourseId(Long courseId, Date startDate) ;

    ResponseObject deleteSchedule(Long scheduleId);

    ResponseObject updateSchedule(UpdateScheduleDTO updateScheduleDTO);

    ResponseObject createSchedule(CreateScheduleDTO createScheduleDTO);

    ResponseObject getAllSchedulesByStudentId(String studentId, Date startDate);

}
