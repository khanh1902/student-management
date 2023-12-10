package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbot.tttn.application.enums.ELessonDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "lesson_day")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LessonDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_day_id")
    private Long lessonDayId;

    @Column(name = "day", unique = true)
    @Enumerated(EnumType.STRING)
    private ELessonDay day;

    @JsonIgnore
    @ManyToMany(mappedBy = "lessonDays", fetch = FetchType.LAZY)
    private List<Courses> courses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "lessonDay", fetch = FetchType.LAZY)
    private Set<Schedule> schedules;


    public LessonDay(ELessonDay day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "[" +
                "lessonDayId=" + lessonDayId +
                ", day=" + day +
                ']';
    }
}
