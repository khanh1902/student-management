package com.springbot.tttn.domain.services;

import com.springbot.tttn.application.entities.Subject;
import com.springbot.tttn.domain.dtos.subjects.DeleteSubjectsDTO;
import com.springbot.tttn.domain.dtos.subjects.SubjectDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {
    ResponseObject findAll(
            Integer pageNo,
            Integer pageSize,
            String sortBy,
            String className,
            boolean asc
    );
    ResponseObject save(SubjectDTO subjectDTO);
    ResponseObject update(SubjectDTO subjectDTO, Long subjectId);
    ResponseObject delete(List<Long> subjectIds);
    ResponseObject getSubjectIdAndName();
    ResponseObject getSubjectRegisterForStudent(String studentId);
}
