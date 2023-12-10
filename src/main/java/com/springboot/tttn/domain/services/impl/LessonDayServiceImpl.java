package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.LessonDay;
import com.springbot.tttn.application.enums.ELessonDay;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.LessonDayService;
import com.springbot.tttn.infrastructure.repositories.LessonDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LessonDayServiceImpl implements LessonDayService {

    @Autowired
    private LessonDayRepository lessonDayRepository;

    @Override
    public LessonDay findLessonDayByName(String name) {
        LessonDay isLessonDay = null;

        switch (name.toLowerCase()) {
            case "monday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.MONDAY);
            case "tuesday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.TUESDAY);
            case "wednesday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.WEDNESDAY);
            case "thursday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.THURSDAY);
            case "friday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.FRIDAY);
            case "saturday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.SATURDAY);
            default -> {
                return null;
            }
        }

        return isLessonDay;
    }
}
