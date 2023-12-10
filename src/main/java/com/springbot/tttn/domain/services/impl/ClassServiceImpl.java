package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.application.entities.Courses;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.classes.ResponseClass;
import com.springbot.tttn.domain.services.ClassService;
import com.springbot.tttn.infrastructure.repositories.ClassRepository;
import com.springbot.tttn.infrastructure.repositories.CoursesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {
    private final Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);
    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Override
    public ResponseObject findAll(Integer pageNo, Integer pageSize, String sortBy, String className, boolean asc) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (sortBy != null) {
            pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        }

        className = className == null ? null : "%" + className + "%";

        Page<ResponseClass> page = classRepository.getAllClasses(className, pageable);

        return new ResponseObject(HttpStatus.OK, new Result("Get all classes successfully", Helper.PageToMap(page)));
    }

    @Override
    public ResponseObject save(Class class_) {
        logger.info("Action: Add new class");
        Class isClass = classRepository.findByClassName(class_.getClassName());
        if (isClass != null) {
            logger.info("Error: Class already exists");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Class already exists", null));
        }
        logger.info("Create new class: " + class_);
        Class createClass = classRepository.save(class_);
        return new ResponseObject(HttpStatus.CREATED, new Result("Save class successfully", createClass));
    }

    @Override
    public ResponseObject update(Class newClass, Long classId) {
        logger.info("Action: Update class");

        Class isClassName = classRepository.findByClassName(newClass.getClassName());
        Class isClass = classRepository.findByClassId(classId);

        if(isClass==null) {
            logger.info("Error: class not found");
            return new ResponseObject(HttpStatus.NOT_FOUND,new Result("Class with this id not found",null));
        }

        if(!isClassName.getClassId().equals(isClass.getClassId())) {
            logger.info("Error: Class name" + newClass.getClassName() + "already exists");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Class name already exists", null));
        }


        logger.info("Update class " + isClass + " to " + newClass);
        isClass.setClassName(newClass.getClassName());
        isClass.setSchoolYear(newClass.getSchoolYear());
        return new ResponseObject(HttpStatus.OK, new Result("Update class successfully", classRepository.save(isClass)));
    }

    @Override
    public ResponseObject deleteClasses(List<Long> classIds) {
        logger.info("Action: Delete class");
        if (classIds.size() < 2) {
            Class isClass = classRepository.findByClassId(classIds.get(0));
            List<Courses> courses = coursesRepository.findAllByClassId(classIds.get(0));

            if(!courses.isEmpty()) {
                return new ResponseObject(HttpStatus.BAD_REQUEST,new Result("Can't delete this class. Please remove courses before delete this class",null));
            }

            logger.info("Delete class: " + isClass.toString());

            classRepository.deleteByClassId(classIds.get(0));
            return new ResponseObject(HttpStatus.OK, new Result("Delete class successfully", null));
        }
//        boolean isDelete = false;
//        for (Long classId : classIds) {
//            Class isClass = classRepository.findByClassId(classId);
//            List<Courses> courses = coursesRepository.findAllByClassId(classId)
//
//
//
//            if (isClass == null) {
//                logger.info("Error: Class id " + classId + " does not exists to delete");
//                continue;
//            }
//            isDelete = true;
//            logger.info("Delete class: " + isClass);
//            classRepository.deleteByClassId(classId);
//        }
//        if (!isDelete) {
//            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Error: Classes does not exists to delete", null));
//        }
        return new ResponseObject(HttpStatus.OK, new Result("Delete class successfully", null));
    }

    @Override
    public ResponseObject findAllForSelect() {
        List<Class> classrooms = classRepository.findAll();

        return new ResponseObject(HttpStatus.OK, new Result("Get all classes successfully", classrooms));
    }


    @Override
    public ResponseObject findClassByName(String className) {
        Class classroom = classRepository.findByClassName(className);

        if (classroom == null) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Classroom not exist", null));
        }

        return new ResponseObject(HttpStatus.OK, new Result("Get classroom successfully", classroom));
    }
}
