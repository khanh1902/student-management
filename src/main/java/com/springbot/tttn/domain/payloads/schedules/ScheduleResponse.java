package com.springbot.tttn.domain.payloads.schedules;

import com.springbot.tttn.application.enums.ELessonDay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleResponse {
        Long scheduleId;
        Long courseId;
        Long subjectId;
        Long lessonId;
        Long classId;
        String lessonName;
        String className;
        String subjectName;
        Date dayLearn;

        @Override
        public String toString() {
                return "{" +
                        "courseId=" + courseId +
                        ", subjectName='" + subjectName + '\'' +
                        '}';
        }
}
