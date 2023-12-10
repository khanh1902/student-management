package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.*;
import com.springbot.tttn.application.enums.ELessonDay;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.domain.dtos.schedules.CreateScheduleDTO;
import com.springbot.tttn.domain.dtos.schedules.UpdateScheduleDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.schedules.ScheduleResponse;
import com.springbot.tttn.domain.services.LessonDayService;
import com.springbot.tttn.domain.services.ScheduleService;
import com.springbot.tttn.infrastructure.repositories.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springbot.tttn.infrastructure.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private LessonDayRepository lessonDayRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonDayService lessonDayService;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public ResponseObject getAllSchedulesByCourseId(Long courseId, Date startDate) {
        List<List<ScheduleResponse>> result = new ArrayList<>();


        for (int i = 0; i <= 6; i++) {

            long timestamp = startDate.getTime() + (i) * 24 * 60 * 60 * 1000;

            Date date = new Date(timestamp);

            String stringDate = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + (date.getDate());

            List<ScheduleResponse> schedules = scheduleRepository.findAllScheduleByCourseIdAndDayLearn(courseId, stringDate);

            logger.info(stringDate + ": " + schedules);

            result.add(schedules);
        }

        return new ResponseObject(HttpStatus.OK, new Result("Get all schedules successfully", result));
    }

    @Override
    public ResponseObject deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Schedule not found", null));
        }
        scheduleRepository.delete(schedule);
        return new ResponseObject(HttpStatus.OK, new Result("Delete schedule successfully", null));
    }

    @Override
    public ResponseObject updateSchedule(UpdateScheduleDTO updateScheduleDTO) {


        // check schedule exist
        Schedule schedule = scheduleRepository.findById(updateScheduleDTO.getScheduleId()).orElse(null);

        // schedule not found
        if (schedule == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Schedule not found", null));
        }

        Courses course = coursesRepository.findByCourseId(schedule.getCourses().getCourseId());

        if(course == null){
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Course not found", null));
        }

        if (!course.getStartDate().before(updateScheduleDTO.getDayLearn()) ) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Day learn must be after start date of course", null));
        }



        // check schedule with day learn is exist
        Schedule scheduleWithDayLearn = scheduleRepository.findByDayLearn(updateScheduleDTO.getDayLearn(),schedule.getCourses().getCourseId());

        if (scheduleWithDayLearn != null && !scheduleWithDayLearn.getScheduleId().equals(schedule.getScheduleId())) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Schedule with day learn exist", null));
        }


        LessonDay lessonDay = lessonDayService.findLessonDayByName(updateScheduleDTO.getLessonDay());

        Lesson lesson =  lessonRepository.findById(updateScheduleDTO.getLessonId()).orElse(null);

        if(lesson == null){
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Lesson not found", null));
        }

        schedule.setDayLearn(updateScheduleDTO.getDayLearn());
        schedule.setLessonDay(lessonDay);
        schedule.setLesson(lesson);

        scheduleRepository.save(schedule);

        return new ResponseObject(HttpStatus.OK, new Result("Update schedule successfully", schedule));

    }

    @Override
    public ResponseObject createSchedule(CreateScheduleDTO createScheduleDTO) {

        Courses course = coursesRepository.findByCourseId(createScheduleDTO.getCourseId());


        if(course == null){
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Course not found", null));
        }

        if (!course.getStartDate().before(createScheduleDTO.getDayLearn()) || !course.getEndDate().after(createScheduleDTO.getDayLearn())) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Day learn must be between start date and end date of course", null));
        }

        Schedule scheduleExist = scheduleRepository.findByDayLearn(createScheduleDTO.getDayLearn(),createScheduleDTO.getCourseId());

        if(scheduleExist != null){
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Schedule with this day is exsit", null));
        }

        LessonDay lessonDay = lessonDayService.findLessonDayByName(createScheduleDTO.getLessonDay());


        Lesson lesson = lessonRepository.findById(createScheduleDTO.getLessonId()).orElse(null);

        if(lesson == null){
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Lesson not found", null));
        }

        Subject subject = subjectRepository.findById(createScheduleDTO.getSubjectId()).orElse(null);

        if(subject == null){
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Subject not found", null));
        }

        Schedule schedule = new Schedule();

        schedule.setDayLearn(createScheduleDTO.getDayLearn());
        schedule.setLessonDay(lessonDay);
        schedule.setLesson(lesson);
        schedule.setCourses(course);
        schedule.setSubject(subject);


        scheduleRepository.save(schedule);

        return new ResponseObject(HttpStatus.OK, new Result("Create schedule successfully", schedule));

    }

    @Override
    public ResponseObject getAllSchedulesByStudentId(String studentId, Date startDate) {
        List<List<ScheduleResponse>> result = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {

            long timestamp = startDate.getTime() + (i) * 24 * 60 * 60 * 1000;

            Date date = new Date(timestamp);

            String stringDate = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + (date.getDate());

            List<ScheduleResponse> schedules = scheduleRepository.findAllScheduleByStudentIdAndDayLearn(studentId, stringDate);

            logger.info(stringDate + ": " + schedules);

            result.add(schedules);
        }

        return new ResponseObject(HttpStatus.OK, new Result("Get all schedules successfully", result));
    }


}
