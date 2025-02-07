package ua.kpi.edutrackeradmin.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.dto.course.CourseRequestForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackeradmin.mapper.CourseMapper;
import ua.kpi.edutrackeradmin.repository.CourseRepository;
import ua.kpi.edutrackeradmin.service.CourseService;
import ua.kpi.edutrackeradmin.service.MinioService;
import ua.kpi.edutrackeradmin.service.ProfessorService;
import ua.kpi.edutrackeradmin.service.StudentService;
import ua.kpi.edutrackeradmin.specification.CourseSpecification;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.enums.StatusCourse;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final MinioService minioService;
    private final CourseMapper courseMapper = new CourseMapper();
    @Override
    public Page<CourseResponseViewAll> getAll(int page, int pageSize, String query) {
        log.info("CourseServiceImpl getAll start");
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id")));
        Specification<Course> specification = new CourseSpecification(query);
        Page<CourseResponseViewAll> result = courseMapper.toDtoListForView(courseRepository.findAll(specification, pageable), minioService);
        log.info("CourseServiceImpl getAll finish");
        return result;
    }
    @Override
    public Course getById(Long courseId) {
        log.info("CourseServiceImpl getById start");
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new EntityNotFoundException("Course with id = "+courseId+" not found")
        );
        log.info("CourseServiceImpl getById finish");
        return course;
    }
    @Override
    public CourseResponseForAdd getByIdForAdd(Long id) {
        log.info("CourseServiceImpl getByIdForAdd start");
        CourseResponseForAdd courseResponseForAdd = courseMapper.toDtoForAdd(getById(id), minioService);
        log.info("CourseServiceImpl getByIdForAdd finish");
        return courseResponseForAdd;
    }
    @Override
    @SneakyThrows
    public Long add(CourseRequestForAdd courseRequestForAdd) {
        log.info("CourseServiceImpl add start");
        Course course = courseMapper.toEntityForAdd(courseRequestForAdd, this, professorService, studentService);
        if(courseRequestForAdd.getImage() != null)course.setImage(minioService.putMultipartFile(courseRequestForAdd.getImage()));
        course.setStatusCourse(StatusCourse.ACTIVE);
        Long result = save(course).getId();
        log.info("CourseServiceImpl add finish");
        return result;
    }
    @Override
    public Course save(Course course) {
        log.info("CourseServiceImpl save start");
        Course result = courseRepository.save(course);
        log.info("CourseServiceImpl save finish");
        return result;
    }
    @Override
    public void removeById(long id) {
        log.info("CourseServiceImpl removeById start");
        Course course = getById(id);
        course.setStatusCourse(StatusCourse.DISABLE);
        save(course);
        log.info("CourseServiceImpl removeById finish");
    }
}