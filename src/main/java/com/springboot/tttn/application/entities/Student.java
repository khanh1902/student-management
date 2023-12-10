package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @Column(name = "mssv")
    private String mssv;

    @Column()
    private String name;

    @Column()
    private String address;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Class classroom;


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private Set<CoursesStudents> coursesStudents;

    @Override
    public String toString() {
        return "[" +
                "mssv='" + mssv + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", classroom=" + classroom +
                ", user=" + user +
                ']';
    }
}
