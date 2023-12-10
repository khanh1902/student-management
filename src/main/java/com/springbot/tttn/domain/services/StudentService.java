package com.springbot.tttn.domain.services;

import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.domain.dtos.students.StudentDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    ResponseObject findAll(Integer pageNo, Integer pageSize, String sortBy, String name, boolean asc, String className);

    ResponseObject createStudent(StudentDto studentDto);

    ResponseObject updateStudent(StudentDto studentDto, String mssv);

    ResponseObject deleteStudent(String mssv);

    ResponseObject deleteStudents(List<String> mssvs);

    String generateMssv(Class classroom, int count);


    ResponseObject findStudentByMssv(String mssv);


}
