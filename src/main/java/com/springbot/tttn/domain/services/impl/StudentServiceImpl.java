package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.application.entities.Role;
import com.springbot.tttn.application.entities.Student;
import com.springbot.tttn.application.entities.User;
import com.springbot.tttn.application.enums.ERole;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.domain.dtos.students.StudentDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.students.ResponseStudent;
import com.springbot.tttn.domain.services.StudentService;
import com.springbot.tttn.infrastructure.repositories.ClassRepository;
import com.springbot.tttn.infrastructure.repositories.RoleRepository;
import com.springbot.tttn.infrastructure.repositories.StudentRepository;
import com.springbot.tttn.infrastructure.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);

    @Override
    public ResponseObject findAll(Integer pageNo, Integer pageSize, String sortBy, String name, boolean asc, String className) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (sortBy != null) {
            if (sortBy.equals("className")) {
                sortBy = "classroom.className";
                pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            } else if (sortBy.equals("schoolYear")) {
                sortBy = "classroom.schoolYear";
                pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            } else {
                pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            }
        }

        name = name == null ? null : "%" + name + "%";


        Page<Student> page = studentRepository.getAllStudents(name, className, pageable);

        return new ResponseObject(HttpStatus.OK, new Result("Get all students successfully", Helper.PageToMap(page)));
    }

    @Override
    public ResponseObject createStudent(StudentDto studentDto) {
        logger.info("Action: Create Student");

        Student student = studentDto.toStudent();

        Class classExist = classRepository.findByClassName(studentDto.getClassName());

        if (classExist == null) {
            logger.error("Error: Class " + studentDto.getClassName() + " does not exists to create student!");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Classroom is not exist", null));
        }

        String mssv = this.generateMssv(classExist, 0);

        Student studentExist = studentRepository.findStudentByMssv(mssv);
        int count = 0;

        while (studentExist != null) {
            count++;
            mssv = this.generateMssv(classExist, count);
            studentExist = studentRepository.findStudentByMssv(mssv);
        }

        student.setClassroom(classExist);
        student.setMssv(mssv);

        Set<Role> setRole = new HashSet<>();
        Role roleExists = roleRepository.findByName(ERole.ROLE_STUDENT);
        setRole.add(roleExists != null ? roleExists : roleRepository.save(new Role(ERole.ROLE_STUDENT)));

        User user  = new User(
                UUID.randomUUID(),
                mssv,
                student.getName(),
                encoder.encode(mssv), // password
                Helper.generateEmail(mssv),
                setRole
        );

        User saveUser = userRepository.saveAndFlush(user);
        student.setUser(saveUser);


        Student createStudent = studentRepository.save(student);

        ResponseStudent response = new ResponseStudent(createStudent.getMssv(), createStudent.getName(), createStudent.getAddress(), createStudent.getClassroom().getClassName(), createStudent.getClassroom().getClassId(), createStudent.getClassroom().getSchoolYear());

        logger.info("Create Student: " + createStudent.toString());

        return new ResponseObject(HttpStatus.CREATED, new Result("Save student successfully", response));
    }

    @Override
    public ResponseObject updateStudent(StudentDto studentDto, String mssv) {
        Student student = studentDto.toStudent();
        student.setMssv(mssv);

        Student studentExist = studentRepository.findStudentByMssv(student.getMssv());

        if (studentExist == null) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Student not exists", null));
        }

        Class classroom = classRepository.findByClassName(studentDto.getClassName());
        studentExist.setMssv(student.getMssv());
        studentExist.setName(student.getName());
        studentExist.setAddress(student.getAddress());
        studentExist.setClassroom(classroom);

        Student updateStudent = studentRepository.save(studentExist);
        ResponseStudent response = new ResponseStudent(updateStudent.getMssv(), updateStudent.getName(), updateStudent.getAddress(), updateStudent.getClassroom().getClassName(), updateStudent.getClassroom().getClassId(), updateStudent.getClassroom().getSchoolYear());

        return new ResponseObject(HttpStatus.CREATED, new Result("Update student successfully", response));

    }

    @Override
    public ResponseObject deleteStudent(String mssv) {
        Student studentExist = studentRepository.findStudentByMssv(mssv);

        if (studentExist == null) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Student not exists", null));
        }

        studentRepository.delete(studentExist);

        return new ResponseObject(HttpStatus.OK, new Result("Delete student successfully", null));
    }

    @Override
    public ResponseObject deleteStudents(List<String> mssvs) {
        List<Student> studentExists = studentRepository.getManyStudentByMssv(mssvs);

        if (studentExists.size() != mssvs.size()) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Some students not exist", null));
        }

        studentRepository.deleteStudentsByMssv(mssvs);

        return new ResponseObject(HttpStatus.OK, new Result("Delete students successfully", null));
    }

    @Override
    public String generateMssv(Class classroom, int count) {
        List<Student> students = studentRepository.getStudentsByClassroom_ClassId(classroom.getClassId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));

        return classroom.getSchoolYear() + month + day + String.format("%03d", students.size() + count);
    }

    @Override
    public ResponseObject findStudentByMssv(String mssv) {
        Student student = studentRepository.findStudentByMssv(mssv);

        if (student == null) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Student not exists", null));
        }

        ResponseStudent response = new ResponseStudent(student.getMssv(), student.getName(), student.getAddress(), student.getClassroom().getClassName(), student.getClassroom().getClassId(), student.getClassroom().getSchoolYear());

        return new ResponseObject(HttpStatus.OK, new Result("Get student successfully", response));
    }


}
