package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Class {
    @Id
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @Column(name = "class_name", unique = true)
    private String className;

    @Column(name = "school_year")
    private Long schoolYear;

    @JsonIgnore
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    private List<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.REMOVE)
    private List<Courses> courses;

    public Class(String className, Long schoolYear) {
        this.className = className;
        this.schoolYear = schoolYear;
    }

    public Class(Long classId, String className, Long schoolYear) {
        this.classId = classId;
        this.className = className;
        this.schoolYear = schoolYear;
    }

    @Override
    public String toString() {
        return "className='" + this.className + '\'' +
                ", schoolYear='" + this.schoolYear + '\'';
    }
}

