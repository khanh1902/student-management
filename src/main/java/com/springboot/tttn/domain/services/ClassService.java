package com.springbot.tttn.domain.services;

import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.domain.payloads.ResponseObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService {
    ResponseObject findAll(
            Integer pageNo,
            Integer pageSize,
            String sortBy,
            String className,
            boolean asc
    );
    ResponseObject save(Class class_);
    ResponseObject update (Class newClass, Long classId);

    ResponseObject findAllForSelect();

    ResponseObject deleteClasses(List<Long> classIds);

    ResponseObject findClassByName(String className);
}
