package com.springbot.tttn.domain.services.impl;

import com.springbot.tttn.application.constants.Constants;
import com.springbot.tttn.application.entities.Class;
import com.springbot.tttn.application.entities.*;
import com.springbot.tttn.application.enums.ELessonDay;
import com.springbot.tttn.application.utils.Helper;
import com.springbot.tttn.domain.dtos.courses.CancelRegisteredCoursesDTO;
import com.springbot.tttn.domain.dtos.courses.CourseDTO;
import com.springbot.tttn.domain.dtos.courses.RegisterCourseForStudentsDTO;
import com.springbot.tttn.domain.dtos.courses.RegisterCoursesDTO;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.payloads.courses.CourseInfoResponse;
import com.springbot.tttn.domain.payloads.courses.CourseResponse;
import com.springbot.tttn.domain.payloads.courses.CoursesResponseForAdmin;
import com.springbot.tttn.domain.payloads.courses.SchedulesOfCourseResponse;
import com.springbot.tttn.domain.payloads.students.StudentYetRegisterResponse;
import com.springbot.tttn.domain.services.CoursesService;
import com.springbot.tttn.infrastructure.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CoursesServiceImpl implements CoursesService {
    private final Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonDayRepository lessonDayRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CoursesStudentsRepository coursesStudentsRepository;

    @Override
    public ResponseObject getCoursesBySubjectIdForStudent(String studentId, Long subjectId) {
        logger.info("Action: Get courses by subject");
        Subject isSubject = subjectRepository.findBySubjectId(subjectId);
        if (isSubject == null) {
            logger.info("ERROR: Subject " + subjectId + " does not exists");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Subject does not exists", null));
        }
        Student isStudent = studentRepository.findStudentByMssv(studentId);

        List<Courses> listCoursesBySubject = coursesRepository.findBySubjectId(subjectId);
        if (listCoursesBySubject.isEmpty()) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course is empty", null));
        }
        List<CourseResponse> result = new ArrayList<>();
        for (Courses course : listCoursesBySubject) {
            CourseResponse courseResponse = new CourseResponse();
            courseResponse.setCourseId(course.getCourseId() + " - " + course.getClassroom().getClassName());
            courseResponse.setSubjectName(course.getSubject().getSubjectName());
            courseResponse.setRegistered(coursesStudentsRepository.countByCourseId(course.getCourseId()) + "/" + course.getMaxStudents());
            courseResponse.setStatus(course.getStatus());
            courseResponse.setRecommend(course.getClassroom().equals(isStudent.getClassroom()));

            List<SchedulesOfCourseResponse> schedulesOfCourse = new ArrayList<>();
            for (LessonDay lessonDay : course.getLessonDays()) {
                List<Schedule> schedules = scheduleRepository.findByCourseIdAndLessonDayId(course.getCourseId(), lessonDay.getLessonDayId());
                schedulesOfCourse.add(new SchedulesOfCourseResponse(Helper.upperFirstCharacter(lessonDay.getDay().name()) + " (Lesson " + course.getLesson().getLessonName() + ")", Helper.formatDate(schedules.get(0).getDayLearn()) + " - " + Helper.formatDate(schedules.get(schedules.size() - 1).getDayLearn())));
            }
            courseResponse.setSchedulesOfCourse(schedulesOfCourse);
            result.add(courseResponse);
        }
        return new ResponseObject(HttpStatus.OK, new Result("Find course successfully", result));
    }

    @Override
    public ResponseObject getCoursesForAdmin(Integer pageNo, Integer pageSize, String sortBy, String subjectName, boolean asc) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            if (sortBy != null) {
                if (sortBy.contains("subjectName")) sortBy = "subject";
                else if (sortBy.contains("startDate")) sortBy = "startDate";
                else if (sortBy.contains("classroom")) sortBy = "classroom";
                else sortBy = "endDate";

                pageable = PageRequest.of(pageNo, pageSize, asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            }

            Page<CoursesResponseForAdmin> page = coursesRepository.findAllBySubjectName(subjectName, pageable);
            for (CoursesResponseForAdmin course : page.getContent()) {
                Courses findCourse = coursesRepository.findByCourseId(course.getCourseId());
                course.setLessonDays(findCourse.getLessonDays());
            }
            return new ResponseObject(HttpStatus.OK, new Result("Get all successfully", Helper.PageToMap(page)));
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.NOT_IMPLEMENTED, new Result(e.getMessage(), null));
        }
    }

    @Override
    public ResponseObject getStudentOfClassYetRegisterCourse(Long courseId, Long classId) {
        Courses isCourse = coursesRepository.findByCourseId(courseId);

        if (isCourse == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not exists", null));
        }

        if (classId == null) classId = isCourse.getClassroom().getClassId();

        Class isClass = classRepository.findByClassId(classId);

        if (isClass == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Class does not exists", null));
        }

        List<Student> getStudentByClass = studentRepository.getStudentsByClassroom_ClassId(classId);

        List<StudentYetRegisterResponse> result = new ArrayList<>();

        for (Student student : getStudentByClass) {
            CoursesStudents isCourseStudent = coursesStudentsRepository.findByCourseIdAndStudentId(courseId, student.getMssv());

            StudentYetRegisterResponse studentYetRegisterResponse = new StudentYetRegisterResponse(student.getMssv(), student.getName());

            List<CoursesStudents> getCourseByStudentId = coursesStudentsRepository.findByStudentId(student.getMssv());

            if (!getCourseByStudentId.isEmpty()) {
                for (CoursesStudents checkDuplicateLesson : getCourseByStudentId) {
                    if (!Objects.equals(checkDuplicateLesson.getCourses().getLesson().getLessonId(), isCourse.getLesson().getLessonId()) && !student.getCoursesStudents().contains(isCourseStudent)) {
                        // skip if exists student in result
                        if (result.contains(studentYetRegisterResponse)) continue;

                        result.add(studentYetRegisterResponse);
                    }
                }
            } else {
                if (!student.getCoursesStudents().contains(isCourseStudent)) {
                    // skip if exists student in result
                    if (result.contains(studentYetRegisterResponse)) continue;

                    result.add(studentYetRegisterResponse);
                }
            }

        }

        return new ResponseObject(HttpStatus.OK, new Result("Get successfully", result));
    }

    @Override
    public ResponseObject create(CourseDTO courseDTO) {
        logger.info("Action: Create new course");
        Subject isSubject = subjectRepository.findBySubjectId(courseDTO.getSubjectId());
        if (isSubject == null) {
            logger.info("ERROR: Subject " + courseDTO.getSubjectId() + " does not exists");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Subject does not exists", null));
        }

        Lesson isLesson = lessonRepository.findByScheduleId(courseDTO.getLessonId());
        if (isLesson == null) {
            logger.info("ERROR: Lesson " + courseDTO.getLessonId() + " does not exists");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Schedule does not exists", null));
        }

        Class isClass = classRepository.findByClassId(courseDTO.getClassId());
        if (isClass == null) {
            logger.info("ERROR: Class " + courseDTO.getClassId() + " does not exists");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Class does not exists", null));
        }

        Courses isCourse = coursesRepository.findBySubjectIdAndClassId(isSubject.getSubjectId(), isClass.getClassId());
        if (isCourse != null) {
            logger.info("ERROR: The course has been opened");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("The course has been opened", null));
        }

        List<LessonDay> lessonDays = new ArrayList<>();
        for (String lessonDay : courseDTO.getLessonDays()) {
            LessonDay isLessonDay = null;
            switch (lessonDay.toLowerCase()) {
                case "monday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.MONDAY);
                case "tuesday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.TUESDAY);
                case "wednesday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.WEDNESDAY);
                case "thursday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.THURSDAY);
                case "friday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.FRIDAY);
                case "saturday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.SATURDAY);
                case "sunday" -> isLessonDay = lessonDayRepository.findByDay(ELessonDay.SUNDAY);
                default -> {
                }
            }
            if (isLessonDay != null) {
                lessonDays.add(isLessonDay);
            }
        }

        if (lessonDays.isEmpty()) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Lesson Day learn is empty", null));
        }

        Date startDate = generateStartDate(courseDTO.getStartDate());
        Date endDate = generateEndDate(startDate, isSubject.getCredit(), (long) courseDTO.getLessonDays().size());


        List<Courses> findBySubjectIdAndLessonId = coursesRepository.findBySubjectIdAndLessonId(isSubject.getSubjectId(), isLesson.getLessonId());

        // check duplicate
        if (!findBySubjectIdAndLessonId.isEmpty()) {
            for (Courses courses : findBySubjectIdAndLessonId) {
                if (compareLists(courses.getLessonDays(), lessonDays) && courses.getClassroom().getClassId().equals(isClass.getClassId())) {
                    return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Duplicate courses. Please change other class", null));
                }
            }
        }

        Courses courses = new Courses(isSubject, isClass, isLesson, lessonDays, startDate, endDate, courseDTO.getMaxStudents());
        Courses newCourse = coursesRepository.saveAndFlush(courses);

        lessonDays = sortLessonDayCreateCourse(lessonDays, startDate);

        // handle schedules;
        for (int i = 0; i < lessonDays.size(); i++) {
            if (i > 0) {
                int instanceDay = (int) (lessonDays.get(i).getLessonDayId() - lessonDays.get(i - 1).getLessonDayId());
                if (instanceDay <= 0) instanceDay += Constants.DAYS_OF_WEEK;
                startDate = addDate(startDate, instanceDay);
            }

            // Calculate day learn of each lesson day
            int maxDayOfLessonDay = (int) (isSubject.getCredit() * Constants.NUMBER_OF_DAYS_LEARN_1_CREDIT / lessonDays.size()) + (isSubject.getCredit() * Constants.NUMBER_OF_DAYS_LEARN_1_CREDIT % lessonDays.size() == 0 ? 0 : 1);

            Date dayLearn = startDate;
            for (int j = 0; j < maxDayOfLessonDay; j++) {
                // save first day learn of each lesson day
                if (j != 0) {
                    // Save day for the next learn
                    dayLearn = addDate(dayLearn, Constants.DAYS_OF_WEEK);
                    if (dayLearn.compareTo(endDate) > 0) break;
                }

                Schedule schedule = new Schedule(newCourse, isSubject, isLesson, dayLearn, lessonDays.get(i));
                scheduleRepository.save(schedule);
            }
        }

        logger.info("Create new course " + newCourse.toString());
        return new ResponseObject(HttpStatus.OK, new Result("Save course successfully", null));
    }

    @Override
    public ResponseObject updateStatus(Long courseId, String status) {
        logger.info("Action: Update status course");
        Courses course = coursesRepository.findByCourseId(courseId);
        if (course == null) {
            logger.info("Error: The course does not exists");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("The course does not exists", null));
        }

        if (status.contains("blocked")) status = Constants.BLOCKED;
        else status = Constants.PENDING;

        course.setStatus(status);
        logger.info("The course is " + status);
        coursesRepository.save(course);
        return new ResponseObject(HttpStatus.OK, new Result("The course is " + status, course));
    }

    @Override
    public ResponseObject deleteCourse(List<Long> courseIds) {
        logger.info("Action: Delete courses");
        if (courseIds.size() < 2) {
            Courses isCourses = coursesRepository.findByCourseId(courseIds.get(0));

            logger.info("Delete course: " + isCourses.toString());

            deleteSchedules(isCourses.getSchedules());

            deleteCoursesStudents(isCourses.getCoursesStudents());

            coursesRepository.deleteByCourseId(isCourses.getCourseId());

            return new ResponseObject(HttpStatus.OK, new Result("Delete course successfully", null));
        }

        boolean isDelete = false;
        for (Long courseId : courseIds) {
            Courses isCourse = coursesRepository.findByCourseId(courseId);
            if (isCourse == null) {
                logger.info("Error: Course id " + courseId + " does not exists to delete");
                continue;
            }

            isDelete = true;

            logger.info("Delete course: " + isCourse.toString());

            deleteSchedules(isCourse.getSchedules());

            deleteCoursesStudents(isCourse.getCoursesStudents());

            coursesRepository.deleteByCourseId(courseId);
        }

        if (!isDelete) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Courses does not exists to delete", null));
        }

        return new ResponseObject(HttpStatus.OK, new Result("Delete course successfully", null));
    }

    @Override
    public ResponseObject registerCourseByStudent(RegisterCoursesDTO registerCoursesDTO) {
        logger.info("Action: Register courses by student");
        CoursesStudents coursesStudents = registerCoursesDTO.toCoursesStudents();

        CoursesStudents isCoursesStudents = coursesStudentsRepository.findByCourseIdAndStudentId(coursesStudents.getId().getCourseId(), coursesStudents.getId().getStudentId());
        if (isCoursesStudents != null) {
            logger.info("Error: Student registered for the course");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Student registered for the course", null));
        }

        Student isStudent = studentRepository.findStudentByMssv(coursesStudents.getId().getStudentId());
        if (isStudent == null) {
            logger.info("Error: Student does not exists to register the course");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student does not exists to register the course", null));
        }

        Courses isCourses = coursesRepository.findByCourseId(coursesStudents.getId().getCourseId());
        if (isCourses == null) {
            logger.info("Error: Course does not exists to register");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not exists to register", null));
        }

        Long studentsRegisteredCourse = coursesStudentsRepository.countByCourseId(coursesStudents.getId().getCourseId());
        if (studentsRegisteredCourse > isCourses.getMaxStudents()) {
            logger.info("Error: The course size is full");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("The course sie is full", null));
        }

        // check duplicate course like lesson
        List<Courses> getCoursesByLesson = coursesRepository.findByLessonId(isCourses.getLesson().getLessonId());
        for (Courses checkDuplicateLesson : getCoursesByLesson) {
            if (isCourses.equals(checkDuplicateLesson)) continue; // skip like course

            if (!isStudent.getCoursesStudents().isEmpty() && isCourses.getStartDate().compareTo(checkDuplicateLesson.getStartDate()) >= 0 && isCourses.getStartDate().compareTo(checkDuplicateLesson.getEndDate()) <= 0) {
                logger.info("Error: Duplicate with other course");
                return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Duplicate with other course", null));
            }
        }

        coursesStudents.setStudent(isStudent);
        coursesStudents.setCourses(isCourses);
        logger.info("Student register the course successfully");
        coursesStudentsRepository.save(coursesStudents);
        return new ResponseObject(HttpStatus.OK, new Result("Student register the course successfully", coursesStudents));
    }

    @Override
    public ResponseObject registerSubjectForStudents(RegisterCourseForStudentsDTO registerCourseForStudentsDTO) {
        logger.info("Action: Register courses for students by admin");

        Courses isCourse = coursesRepository.findByCourseId(registerCourseForStudentsDTO.getCourseId());
        if (isCourse == null) {
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not found", null));
        }

        List<String> studentAdded = new ArrayList<>();
        for (String studentId : registerCourseForStudentsDTO.getStudents()) {
            Student isStudent = studentRepository.findStudentByMssv(studentId);
            if (isStudent == null) {
                logger.info("Student id " + studentId + " not found");
                continue;
            }

            CoursesStudents isCoursesStudents = coursesStudentsRepository.findByCourseIdAndStudentId(registerCourseForStudentsDTO.getCourseId(), studentId);
            if (isCoursesStudents != null) {
                logger.info("Student registered for the course");
                continue;
            }

            CoursesStudents coursesStudents = new CoursesStudents(new CoursesStudentsKey(isCourse.getCourseId(), studentId), isCourse, isStudent, null, null, null);
            coursesStudentsRepository.save(coursesStudents);
            studentAdded.add(studentId);
        }

        if (studentAdded.isEmpty()) {
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Register course for students failed", null));
        }

        logger.info("Add students to course successfully");
        registerCourseForStudentsDTO.setStudents(studentAdded);
        return new ResponseObject(HttpStatus.OK, new Result("Add students to course successfully", registerCourseForStudentsDTO));
    }

    @Override
    public ResponseObject registerCourseByAdmin(RegisterCoursesDTO registerCoursesDTO) {
        logger.info("Action: Register courses for student by admin");
        CoursesStudents coursesStudents = registerCoursesDTO.toCoursesStudents();

        CoursesStudents isCoursesStudents = coursesStudentsRepository.findByCourseIdAndStudentId(coursesStudents.getId().getCourseId(), coursesStudents.getId().getStudentId());
        if (isCoursesStudents != null) {
            logger.info("Error: Student registered for the course");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Student registered for the course", null));
        }

        Student isStudent = studentRepository.findStudentByMssv(coursesStudents.getId().getStudentId());
        if (isStudent == null) {
            logger.info("Error: Student does not exists to register the course");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student does not exists to register the course", null));
        }

        Courses isCourses = coursesRepository.findByCourseId(coursesStudents.getId().getCourseId());
        if (isCourses == null) {
            logger.info("Error: Course does not exists to register");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not exists to register", null));
        }

        // check duplicate course like lesson
        List<Courses> getCoursesByLesson = coursesRepository.findByLessonId(isCourses.getLesson().getLessonId());
        getCoursesByLesson.remove(isCourses); // skip the course
        for (Courses checkDuplicateCourse : getCoursesByLesson) {
            if (isCourses.getStartDate().compareTo(checkDuplicateCourse.getStartDate()) >= 0 && isCourses.getStartDate().compareTo(checkDuplicateCourse.getEndDate()) <= 0) {
                logger.info("Error: Duplicate with other course");
                return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Duplicate with other course", null));
            }
        }

        coursesStudents.setStudent(isStudent);
        coursesStudents.setCourses(isCourses);
        logger.info("Student register the course successfully");
        coursesStudentsRepository.save(coursesStudents);
        return new ResponseObject(HttpStatus.OK, new Result("Admin register the course for student successfully", coursesStudents));
    }

    @Override
    public ResponseObject cancelCourseRegistered(CancelRegisteredCoursesDTO cancelRegisteredCoursesDTO) {
        logger.info("Action: Cancel course registered");
        CoursesStudents coursesStudents = cancelRegisteredCoursesDTO.toCoursesStudents();

        Student isStudent = studentRepository.findStudentByMssv(coursesStudents.getId().getStudentId());
        if (isStudent == null) {
            logger.info("Error: Student does not exists to register the course");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Student does not exists to register the course", null));
        }

        Courses isCourses = coursesRepository.findByCourseId(coursesStudents.getId().getCourseId());
        if (isCourses == null) {
            logger.info("Error: Course does not exists to register");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not exists to register", null));
        }

        CoursesStudents isCoursesStudents = coursesStudentsRepository.findByCourseIdAndStudentId(coursesStudents.getId().getCourseId(), coursesStudents.getId().getStudentId());
        if (isCoursesStudents == null) {
            logger.info("Error: Student yet register for the course");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("Student registered for the course", null));
        }

        Date expiredDateCancel = new Date();
        if (expiredDateCancel.compareTo(addDate(isCourses.getStartDate(), 14)) >= 0 || isCourses.getStatus().equals(Constants.BLOCKED)) {
            logger.info("Error: The course is locked");
            return new ResponseObject(HttpStatus.BAD_REQUEST, new Result("The course is locked", null));
        }

        logger.info("Cancel the registered course successfully");
        coursesStudentsRepository.deleteByCourseIdAndStudentId(coursesStudents.getId().getCourseId(), coursesStudents.getId().getStudentId());

        return new ResponseObject(HttpStatus.OK, new Result("Cancel the registered course successfully", null));
    }

    @Override
    public ResponseObject getCourseById(Long courseId) {
        logger.info("Action: Get course by id");
        Courses course = coursesRepository.findByCourseId(courseId);

        if (course == null) {
            logger.info("Error: Course does not exists");
            return new ResponseObject(HttpStatus.NOT_FOUND, new Result("Course does not exists", null));
        }

        Subject subject = subjectRepository.findBySubjectId(course.getSubject().getSubjectId());
        Class classroom = classRepository.findByClassId(course.getClassroom().getClassId());

        CourseInfoResponse courseInfoResponse = new CourseInfoResponse(course.getCourseId(), subject.getSubjectName(), subject.getSubjectId(), classroom.getClassName(),classroom.getClassId());

        return new ResponseObject(HttpStatus.OK, new Result("Get course successfully", courseInfoResponse));
    }


    Date generateStartDate(String startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(startDate);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    Date generateEndDate(Date dateStart, Long credit, Long lessonDayLength) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateStart);

            // count sum day learn
            int sumDayLearn = (int) (credit * 15);

            int countDayLearn = (int) (7 * sumDayLearn / lessonDayLength);

            calendar.add(Calendar.DAY_OF_YEAR, countDayLearn);

            String endDate = dateFormat.format(calendar.getTime());

            return dateFormat.parse(endDate);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    Date addDate(Date date, int add) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.add(Calendar.DAY_OF_YEAR, add);
            return dateFormat.parse(dateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    void deleteCoursesStudents(List<CoursesStudents> listCoursesStudents) {
        if (!listCoursesStudents.isEmpty()) {
            for (CoursesStudents coursesStudents : listCoursesStudents) {
                coursesStudentsRepository.deleteByCourseId(coursesStudents.getCourses().getCourseId());
            }
        }
    }

    void deleteSchedules(List<Schedule> listSchedule) {
        if (!listSchedule.isEmpty()) {
            for (Schedule schedule : listSchedule) {
                scheduleRepository.deleteByCourseId(schedule.getCourses().getCourseId());
            }
        }
    }

    public static boolean compareLists(List<?> list1, List<?> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        HashSet<Object> set1 = new HashSet<>(list1);
        HashSet<Object> set2 = new HashSet<>(list2);

        return set1.equals(set2);
    }

    List<LessonDay> sortLessonDayCreateCourse(List<LessonDay> lessonDays, Date startDay) {
        int firstLessonDay = startDay.getDay();
        List<LessonDay> tempLess = new ArrayList<>();
        List<LessonDay> tempBiggerOfEqual = new ArrayList<>();
        for (LessonDay lessonDay : lessonDays) {
            if (lessonDay.getLessonDayId() >= firstLessonDay) {
                tempBiggerOfEqual.add(lessonDay);
            } else {
                tempLess.add(lessonDay);
            }
        }
        tempBiggerOfEqual.addAll(tempLess);
        return tempBiggerOfEqual;
    }
}
