package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.LessonDay;
import com.springbot.tttn.application.entities.Lesson;
import com.springbot.tttn.application.enums.ELessonDay;
import com.springbot.tttn.infrastructure.repositories.LessonDayRepository;
import com.springbot.tttn.infrastructure.repositories.LessonRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBInit {
    @Autowired
    private LessonDayRepository lessonDayRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private final Logger logger = LoggerFactory.getLogger(DBInit.class);

    @PostConstruct
    public void generateLessonDay() {
        try {
            List<LessonDay> findAll = lessonDayRepository.findAll();
            if (findAll.isEmpty()) {
                lessonDayRepository.save(new LessonDay(ELessonDay.SUNDAY));
                lessonDayRepository.save(new LessonDay(ELessonDay.MONDAY));
                lessonDayRepository.save(new LessonDay(ELessonDay.TUESDAY));
                lessonDayRepository.save(new LessonDay(ELessonDay.WEDNESDAY));
                lessonDayRepository.save(new LessonDay(ELessonDay.THURSDAY));
                lessonDayRepository.save(new LessonDay(ELessonDay.FRIDAY));
                lessonDayRepository.save(new LessonDay(ELessonDay.SATURDAY));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @PostConstruct
    public void generateSchedules() {
        try {
            List<Lesson> findAll = lessonRepository.findAll();
            if (findAll.isEmpty()) {
                lessonRepository.save(new Lesson("1-3"));
                lessonRepository.save(new Lesson("4-6"));
                lessonRepository.save(new Lesson("7-9"));
                lessonRepository.save(new Lesson("10-12"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
