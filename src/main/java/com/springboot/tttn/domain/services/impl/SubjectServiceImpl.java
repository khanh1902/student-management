package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.CoursesStudents;
import com.springbot.tttn.application.entities.Student;
import com.springbot.tttn.application.entities.Subject;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.domain.dtos.subjects.SubjectDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.subjects.SubjectIdCreditAndNameResponse;
import com.springbot.tttn.domain.payloads.subjects.SubjectResponse;
import com.springbot.tttn.domain.services.SubjectService;
import com.springbot.tttn.infrastructure.repositories.CoursesStudentsRepository;
import com.springbot.tttn.infrastructure.repositories.StudentRepository;
import com.springbot.tttn.infrastructure.repositories.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Override
    public ResponseObject findAll(Integer pageNo, Integer pageSize, String sortBy, String subjectName, boolean asc) {
        try{
            Pageable pageRequest = PageRequest.of(pageNo, pageSize);
            if (sortBy != null) {
                pageRequest = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            }
            else {
                pageRequest = PageRequest.of(pageNo, pageSize, asc ? Sort.by("subjectName").ascending() : Sort.by("subjectName").descending());
            }

            Page<SubjectResponse> findAll = subjectRepository.findAllBySubjectName(subjectName, pageRequest);

            return new ResponseObject(HttpStatus.OK, new Result("Get subjects successfully", Helper.PageToMap(findAll)));
        }catch (Exception e) {
            return new ResponseObject(HttpStatus.OK, new Result("Error", e.getMessage()));
        }
    }

    @Override
    public ResponseObject save(SubjectDTO subjectDTO) {
        logger.info("Action: Add new Subject");
        Subject subject = subjectDTO.toSubject();
        Subject isSubject = subjectRepository.findBySubjectName(subject.getSubjectName());
        if (isSubject != null) {
            logger.info("Error: Subject already exists");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Subject already exists", null));
        }

        Subject createSubject = subjectRepository.save(subject);
        logger.info("New Subject: " + createSubject.toString());

        return new ResponseObject(HttpStatus.CREATED, new Result("Add subject successfully", createSubject));
    }

    @Override
    public ResponseObject update(SubjectDTO subjectDTO, Long subjectId) {
        logger.info("Action: Add new Subject");

        Subject newSubject = subjectDTO.toSubject();

        Subject isSubjectName = subjectRepository.findBySubjectName(newSubject.getSubjectName());
        if (isSubjectName != null) {
            logger.info("Error: Subject " + newSubject.getSubjectName() + " already exists");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Subject already exists", null));
        }

        Subject isSubject = subjectRepository.findBySubjectId(subjectId);
        if (isSubject == null) {
            Subject createSubject = subjectRepository.save(newSubject);
            logger.info("New Subject: " + createSubject.toString());
            return new ResponseObject(HttpStatus.CREATED, new Result("Add subject successfully", createSubject));
        }

        logger.info("Update subject from " + isSubject.toString() + " to " + newSubject.toString());
        isSubject.setSubjectName(newSubject.getSubjectName());
        isSubject.setCredit(newSubject.getCredit());

        return new ResponseObject(HttpStatus.OK, new Result("Update subject successfully", subjectRepository.save(isSubject)));
    }

    @Override
    public ResponseObject delete(List<Long> subjectIds) {
        logger.info("Action: Delete subject");
        if (subjectIds.size() < 2) {
            Subject isSubject = subjectRepository.findBySubjectId(subjectIds.get(0));
            logger.info("Delete subject: " + isSubject.toString());
            isSubject.setIsDelete(true);
            subjectRepository.save(isSubject);
            return new ResponseObject(HttpStatus.OK, new Result("Delete subject successfully", null));
        }
        boolean isDelete = false;
        for (Long subjectId : subjectIds) {
            Subject isSubject = subjectRepository.findBySubjectId(subjectId);
            if (isSubject == null) {
                logger.info("Error: Subject id " + subjectId + " does not exists to delete");
                continue;
            }
            isDelete = true;
            logger.info("Delete class: " + isSubject.toString());
            isSubject.setIsDelete(true);
            subjectRepository.save(isSubject);
        }
        if(!isDelete) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Subjects does not exists to delete", null));
        }
        return new ResponseObject(HttpStatus.OK, new Result("Delete subject successfully", null));
    }

    @Override
    public ResponseObject getSubjectIdAndName() {
        return new ResponseObject(HttpStatus.OK, new Result("Get all successfully", subjectRepository.getIdAndName()));
    }

    @Override
    public ResponseObject getSubjectRegisterForStudent(String studentId) {
        Student isStudent = studentRepository.findStudentByMssv(studentId);
        if(isStudent == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student does not exists", null));
        }

        List<SubjectIdCreditAndNameResponse> result = subjectRepository.getForRegister(studentId);

        return new ResponseObject(HttpStatus.OK, new Result("Get subjects successfully", result));
    }
}