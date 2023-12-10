package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.LessonService;
import com.springbot.tttn.infrastructure.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public ResponseObject getAll() {
        return new ResponseObject(HttpStatus.OK, new Result("Get lesson successfully", lessonRepository.findAll()));
    }
}
