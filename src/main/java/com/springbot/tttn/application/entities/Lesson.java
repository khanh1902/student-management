package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "lesson_name")
    private String lessonName;

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    private List<Courses> courses;

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    private Set<Schedule> schedules;

    public Lesson(String lessonName) {
        this.lessonName = lessonName;
    }

    @Override
    public String toString() {
        return "[" +
                "scheduleId=" + lessonId +
                ", lesson='" + lessonName + '\'' +
                ']';
    }
}
