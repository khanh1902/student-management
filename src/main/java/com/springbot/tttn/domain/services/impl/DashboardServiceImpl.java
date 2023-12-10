package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.constants.ClassificationConstants;
import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.application.entities.CoursesStudents;
import com.springbot.tttn.application.entities.Student;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.dashboard.*;
import com.springbot.tttn.domain.services.DashboardService;
import com.springbot.tttn.infrastructure.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private CoursesStudentsRepository coursesStudentsRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public ResponseObject getStudentDashboard(String studentId) {
        Student student = studentRepository.findStudentByMssv(studentId);

        if (student == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student not found", null));
        }

        Class classOfStudent = classRepository.findByClassId(student.getClassroom().getClassId());

        if (classOfStudent == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Class not found", null));
        }

        Long totalCourse = coursesStudentsRepository.numberCoursesOfStudent(studentId);

        LocalDate startDate = LocalDate.now();

        startDate = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        LocalDate endDate = startDate.plusDays(6);

        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);

        List<Long> listNumberDayLearnInWeek = scheduleRepository.numberDayLearnInWeek(
                studentId,
                Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );

        System.out.println("listNumberDayLearnInWeek: " + listNumberDayLearnInWeek.toString());

        Long totalDayLearnInWeek = listNumberDayLearnInWeek.stream().reduce(0L, Long::sum);

        Long numCredits = coursesStudentsRepository.numberCreditsOfStudent(studentId);

        System.out.println("numCredits: " + numCredits);
        ChartScoresForStudent chartScoresForStudent = chartScoresForStudent(studentId);

        Result result = new Result(
                "Get student dashboard successfully",
                new StudentDashboard(student, new Class(
                        classOfStudent.getClassId(),
                        classOfStudent.getClassName(),
                        classOfStudent.getSchoolYear()
                )
                        , totalCourse,
                        totalDayLearnInWeek,
                        numCredits,
                        chartScoresForStudent.getCumulativeAverageScores(),
                        chartScoresForStudent.getChartDetails())
        );

        return new ResponseObject(HttpStatus.OK, result);

    }

    @Override
    public ResponseObject getAllScoresForStudent(String studentId) {
        Student isStudent = studentRepository.findStudentByMssv(studentId);

        if (Objects.isNull(isStudent)) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student does not exists", null));
        }

        List<ListScoresForStudent> listScores = new ArrayList<>();
        Long totalCreditPassed = 0L;
        Long totalCreditFailed = 0L;
        Double averageScore = 0D;
        int sumCredit = 0;
        Double averageScaleFourScores = 0D;
        String averageClassificationDetails = null;

        List<CoursesStudents> listScoresOfStudent = coursesStudentsRepository.findByStudentId(studentId);

        for (CoursesStudents cs : listScoresOfStudent) {
            if (cs.getScores() == null) continue;
            Boolean checkScoreOfStudentsIsNull = coursesStudentsRepository.isNullScoreOfCourse(cs.getCourses().getCourseId());

            //skip if the course unfinished;
            if (checkScoreOfStudentsIsNull) continue;

            if (cs.getScores() >= 4.0) totalCreditPassed += cs.getCourses().getSubject().getCredit();
            else totalCreditFailed += cs.getCourses().getSubject().getCredit();

            averageScore += cs.getScores() * cs.getCourses().getSubject().getCredit();
            sumCredit += cs.getCourses().getSubject().getCredit();

            listScores.add(new ListScoresForStudent(
                    cs.getId().getCourseId(),
                    cs.getCourses().getSubject().getSubjectId(),
                    cs.getCourses().getSubject().getSubjectName(),
                    cs.getCourses().getSubject().getCredit(),
                    cs.getScores(),
                    cs.getFourScoresScale(),
                    cs.getClassification(),
                    getClassificationDetails(cs.getClassification())
            ));

        }

        averageScore = averageScore / sumCredit * 100 / 100;

        averageScaleFourScores = averageScore * 4 / 10 * 100 / 100;

        if (averageScaleFourScores <= 4.0 && averageScaleFourScores >= 3.6) averageClassificationDetails = ClassificationConstants.EXCELLENT;
        else if (averageScaleFourScores < 3.6 && averageScaleFourScores >= 3.2) averageClassificationDetails = ClassificationConstants.DISTINCTION;
        else if (averageScaleFourScores < 3.2 && averageScaleFourScores >= 2.5) averageClassificationDetails = ClassificationConstants.GOOD;
        else if (averageScaleFourScores < 2.5 && averageScaleFourScores >= 2.0) averageClassificationDetails = ClassificationConstants.AVERAGE;
        else averageClassificationDetails = ClassificationConstants.POOR;

        ListScoresForStudentDetail result = new ListScoresForStudentDetail(
                listScores,
                totalCreditPassed,
                totalCreditFailed,
                averageScore,
                averageScaleFourScores,
                averageClassificationDetails
        );
        return new ResponseObject(HttpStatus.OK, new Result("Get list scores for student successfully", result));
    }

    private String getClassificationDetails(String classification) {
        if (classification.equals(ClassificationConstants.A)) return ClassificationConstants.DISTINCTION;
        else if (classification.equals(ClassificationConstants.B_PLUS) || classification.equals(ClassificationConstants.B))
            return ClassificationConstants.GOOD;
        else if (classification.equals(ClassificationConstants.C_PLUS) || classification.equals(ClassificationConstants.C))
            return ClassificationConstants.AVERAGE;
        else if (classification.equals(ClassificationConstants.D_PLUS) || classification.equals(ClassificationConstants.D))
            return ClassificationConstants.BLOW_AVERAGE;
        else return ClassificationConstants.POOR;
    }

    private ChartScoresForStudent chartScoresForStudent(String studentId) {
        List<ChartScoresForStudentDetails> result = new ArrayList<>();
        List<CoursesStudents> listScoresOfStudent = coursesStudentsRepository.findByStudentId(studentId);
        Double sumScoresOfStudent = 0D;
        int size = 0;
        for (CoursesStudents cs : listScoresOfStudent) {
            if (cs.getScores() == null) continue;

            Boolean checkScoreOfStudentsIsNull = coursesStudentsRepository.isNullScoreOfCourse(cs.getCourses().getCourseId());

            if (checkScoreOfStudentsIsNull) continue;

            Double averageScoreOfCourse = averageScoresOfCourse(coursesStudentsRepository.findByCourseId(cs.getCourses().getCourseId()));

            sumScoresOfStudent += cs.getScores();
            size++;

            result.add(new ChartScoresForStudentDetails(
                    cs.getCourses().getSubject().getSubjectId(),
                    cs.getCourses().getSubject().getSubjectName(),
                    averageScoreOfCourse, cs.getScores())
            );
        }
        Double cumulativeScores = sumScoresOfStudent / size * 100 / 100;

        return new ChartScoresForStudent(cumulativeScores, result);
    }

    private Double averageScoresOfCourse(List<CoursesStudents> coursesStudentsList) {
        if (coursesStudentsList.size() == 1) {
            return coursesStudentsList.get(0).getScores();
        }

        Double sumScores = 0D;

        for (CoursesStudents cs : coursesStudentsList) {
            sumScores += cs.getScores();
        }
        return sumScores / coursesStudentsList.size() * 100 / 100;
    }
}
