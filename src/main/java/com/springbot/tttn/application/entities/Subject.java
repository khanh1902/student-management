package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "subject")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subject_name", nullable = false, unique = true)
    private String subjectName;

    @Column(name = "credit", nullable = false)
    private Long credit;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private Set<Courses> courses;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private Set<Schedule> schedules;

    public Subject(String subjectName, Long credit) {
        this.subjectName = subjectName;
        this.credit = credit;
    }

    public Subject(Long subjectId, String subjectName, Long credit) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.credit = credit;
        this.isDelete = false;
    }

    @Override
    public String toString() {
        return "subjectName='" + subjectName + '\'' +
                ", credit=" + credit;
    }
}