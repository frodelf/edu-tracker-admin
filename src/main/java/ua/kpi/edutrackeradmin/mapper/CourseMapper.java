package ua.kpi.edutrackeradmin.mapper;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ua.kpi.edutrackeradmin.dto.course.CourseRequestForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackeradmin.service.CourseService;
import ua.kpi.edutrackeradmin.service.MinioService;
import ua.kpi.edutrackeradmin.service.ProfessorService;
import ua.kpi.edutrackeradmin.service.StudentService;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class CourseMapper {
    @SneakyThrows
    public CourseResponseViewAll toDtoForView(Course course, MinioService minioService) {
        CourseResponseViewAll courseResponseViewAll = new CourseResponseViewAll();
        courseResponseViewAll.setId(course.getId());
        courseResponseViewAll.setName(course.getName());
        courseResponseViewAll.setGoal(course.getGoal());
        if(course.getStudents()!=null)
            courseResponseViewAll.setNumberOfStudents(course.getStudents().size());
        if(course.getImage()!=null)
            courseResponseViewAll.setImage(minioService.getUrl(course.getImage()));
        return courseResponseViewAll;
    }
    public Page<CourseResponseViewAll> toDtoListForView(Page<Course> courses, MinioService minioService) {
        return new PageImpl<>(
                courses.getContent().stream()
                        .map(course -> toDtoForView(course, minioService))
                        .collect(Collectors.toList()),
                courses.getPageable(),
                courses.getTotalElements()
        );
    }
    public Course toEntityForAdd(CourseRequestForAdd requestAdd, CourseService courseService, ProfessorService professorService, StudentService studentService) {
        Course course = new Course();
        if(nonNull(requestAdd.getId())) course = courseService.getById(requestAdd.getId());
        if(nonNull(requestAdd.getName())) course.setName(requestAdd.getName());
        if(nonNull(requestAdd.getMaximumMark())) course.setMaximumMark(requestAdd.getMaximumMark());
        if(nonNull(requestAdd.getGoal())) course.setGoal(requestAdd.getGoal());
        if(nonNull(requestAdd.getIsForChoosing()))course.setIsForChoosing(requestAdd.getIsForChoosing());
        if(nonNull(requestAdd.getProfessorId()))course.setProfessor(professorService.getById(requestAdd.getProfessorId()));
        if(nonNull(requestAdd.getGroups())){
            List<Student> students = new ArrayList<>();
            for (String group : requestAdd.getGroups()) {
                students.addAll(studentService.getAllStudentsByGroup(group));
            }
            course.setStudents(students);
        }
        return course;
    }
    @SneakyThrows
    public CourseResponseForAdd toDtoForAdd(Course course, MinioService minioService) {
        CourseResponseForAdd courseRequestForAdd = new CourseResponseForAdd();
        courseRequestForAdd.setId(course.getId());
        courseRequestForAdd.setName(course.getName());
        courseRequestForAdd.setMaximumMark(course.getMaximumMark());
        courseRequestForAdd.setGoal(course.getGoal());
        if(nonNull(course.getProfessor())){
            courseRequestForAdd.setProfessorId(course.getProfessor().getId());
            courseRequestForAdd.setProfessorName(course.getProfessor().getLastName()+" "+course.getProfessor().getName());
        }
        if(nonNull(course.getImage()))courseRequestForAdd.setImage(minioService.getUrl(course.getImage()));
        return courseRequestForAdd;
    }
}