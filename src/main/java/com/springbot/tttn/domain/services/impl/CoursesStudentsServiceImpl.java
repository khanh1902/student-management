package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.constants.ClassificationConstants;
import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.application.entities.Courses;
import com.springbot.tttn.application.entities.CoursesStudents;
import com.springbot.tttn.application.entities.Student;
import com.springbot.tttn.application.enums.EClassification;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.coursestudent.CoursesOfStudentResponse;
import com.springbot.tttn.domain.payloads.coursestudent.ScoresOfStudentResponse;
import com.springbot.tttn.domain.payloads.coursestudent.StudentOfCourseResponse;
import com.springbot.tttn.domain.services.CoursesStudentsService;
import com.springbot.tttn.infrastructure.repositories.ClassRepository;
import com.springbot.tttn.infrastructure.repositories.CoursesRepository;
import com.springbot.tttn.infrastructure.repositories.CoursesStudentsRepository;
import com.springbot.tttn.infrastructure.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CoursesStudentsServiceImpl implements CoursesStudentsService {
    private final Logger logger = LoggerFactory.getLogger(CoursesStudentsServiceImpl.class);

    @Autowired
    private CoursesStudentsRepository coursesStudentsRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public ResponseObject getStudentRegisteredCourse(Integer pageNo, Integer pageSize, String sortBy, Boolean asc, Long courseId, String studentName) {
        logger.info("Action: Get students registered course");
        Courses isCourse = coursesRepository.findByCourseId(courseId);

        if (isCourse == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not exists", null));
        }

        if (sortBy == null) {
            sortBy = "student.name";
        } else {
            if (sortBy.contains("studentId")) sortBy = "student.mssv";
            else if (sortBy.contains("studentName")) sortBy = "student.name";
            else if (sortBy.contains("classId")) sortBy = "student.classroom.classId";
            else if (sortBy.contains("className")) sortBy = "student.classroom.className";
            else if (sortBy.contains("scores")) sortBy = "scores";
            else if (sortBy.contains("fourScoresScale")) sortBy = "fourScoresScale";
            else if (sortBy.contains("classification")) sortBy = "classification";
            else sortBy = "student";
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<StudentOfCourseResponse> result = coursesStudentsRepository.findStudentsRegisteredCourse(courseId, studentName, pageable);

        logger.info("Data: " + result.getContent().toString());

        return new ResponseObject(HttpStatus.OK, new Result("Get successfully", Helper.PageToMap(result)));
    }

    @Override
    public ResponseObject getCoursesOfStudentRegistered(Integer pageNo, Integer pageSize, String sortBy, Boolean asc, String studentId) {
        Student isStudent = studentRepository.findStudentByMssv(studentId);

        if (isStudent == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student does not exists", null));
        }

        if (sortBy == null) {
            sortBy = "courses.courseId";
        } else {
            if (sortBy.contains("courseId")) sortBy = "courses.courseId";
            if (sortBy.contains("courseName")) sortBy = "courses.courseName";
            if (sortBy.contains("courseId")) sortBy = "courses.courseId";
            else if (sortBy.contains("scores")) sortBy = "scores";
            else if (sortBy.contains("classification")) sortBy = "classification";
            else sortBy = "student";
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());


        Page<CoursesOfStudentResponse> result = coursesStudentsRepository.findCoursesOfStudentRegistered(studentId, pageable);

        for (CoursesOfStudentResponse course : result.getContent()) {
            Courses findCourse = coursesRepository.findByCourseId(course.getCourseId());
            course.setLessonDays(findCourse.getLessonDays());
        }

        return new ResponseObject(HttpStatus.OK, new Result("Get successfully", Helper.PageToMap(result)));
    }

    @Override
    public ResponseObject updateScores(String studentId, Long courseId, Double scores) {
        logger.info("Action: Update scores");

        Student isStudent = studentRepository.findStudentByMssv(studentId);

        if (isStudent == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student does not exists", null));
        }

        Courses isCourse = coursesRepository.findByCourseId(courseId);

        if (isCourse == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not exists", null));
        }

        CoursesStudents coursesStudents = coursesStudentsRepository.findByCourseIdAndStudentId(courseId, studentId);

        if (coursesStudents == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student of course does not exists", null));
        }

        if (scores > 10.0 || scores < 0) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Scores must from 0 to 10", null));

        }

        coursesStudents.setScores(scores);

        if (scores <= 10 && scores >= 8.5) {
            coursesStudents.setClassification(ClassificationConstants.A);
            coursesStudents.setFourScoresScale(4.0);
        } else if (scores <= 8.4 && scores >= 8.0) {
            coursesStudents.setClassification(ClassificationConstants.B_PLUS);
            coursesStudents.setFourScoresScale(3.5);
        } else if (scores <= 7.9 && scores >= 7.0) {
            coursesStudents.setClassification(ClassificationConstants.B);
            coursesStudents.setFourScoresScale(3.0);
        } else if (scores <= 6.9 && scores >= 6.0) {
            coursesStudents.setClassification(ClassificationConstants.C_PLUS);
            coursesStudents.setFourScoresScale(2.5);
        } else if (scores <= 5.9 && scores >= 5.5) {
            coursesStudents.setClassification(ClassificationConstants.C);
            coursesStudents.setFourScoresScale(2.0);
        } else if (scores <= 5.4 && scores >= 5.0) {
            coursesStudents.setClassification(ClassificationConstants.D_PLUS);
        } else if (scores <= 4.9 && scores >= 4.0) {
            coursesStudents.setClassification(ClassificationConstants.D);
            coursesStudents.setFourScoresScale(1.5);
        } else {
            coursesStudents.setClassification(ClassificationConstants.F);
            coursesStudents.setFourScoresScale(0D);

        }

        coursesStudentsRepository.save(coursesStudents);

        return new ResponseObject(HttpStatus.OK, new Result("Update scores successfully", coursesStudents));
    }

    @Override
    public ResponseObject getScoresByClassAndStudentName(Integer pageNo, Integer pageSize, String sortBy, Boolean asc, Long classId, String studentName) {

        if (classId != null) {
            Class isClass = classRepository.findByClassId(classId);
            if (isClass == null) {
                return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Class does not exists", null));
            }
        }

        if (sortBy == null) {
            sortBy = "student.name";
        } else {
            if (sortBy.contains("studentId")) sortBy = "student.mssv";
            else if (sortBy.contains("studentName")) sortBy = "student.name";
            else if (sortBy.contains("classId")) sortBy = "student.classroom.classId";
            else if (sortBy.contains("className")) sortBy = "student.classroom.className";
            else if (sortBy.contains("courseId")) sortBy = "courses.courseId";
            else if (sortBy.contains("subjectId")) sortBy = "courses.subject.subjectId";
            else if (sortBy.contains("subjectName")) sortBy = "courses.subject.subjectName";
            else if (sortBy.contains("credit")) sortBy = "courses.subject.credit";
            else if (sortBy.contains("scores")) sortBy = "scores";
            else if (sortBy.contains("classification")) sortBy = "classification";
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<ScoresOfStudentResponse> result = coursesStudentsRepository.findScoresOfStudentByStudentIdAndOptionClassId(studentName, classId, pageable);

        return new ResponseObject(HttpStatus.OK, new Result("Get scores successfully", Helper.PageToMap(result)));
    }
}
