package ua.kpi.edutrackeradmin.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.dto.course.CourseRequestForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackeradmin.elasticsearch.CourseElasticsearchRepository;
import ua.kpi.edutrackeradmin.elasticsearch.CourseIndex;
import ua.kpi.edutrackeradmin.mapper.CourseMapper;
import ua.kpi.edutrackeradmin.repository.CourseRepository;
import ua.kpi.edutrackeradmin.service.CourseService;
import ua.kpi.edutrackeradmin.service.MinioService;
import ua.kpi.edutrackeradmin.service.ProfessorService;
import ua.kpi.edutrackeradmin.service.StudentService;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.enums.StatusCourse;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseElasticsearchRepository courseElasticsearchRepository;
    private final CourseRepository courseRepository;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final MinioService minioService;
    private final CourseMapper courseMapper = new CourseMapper();
    @Override
    public Page<CourseResponseViewAll> getAll(int page, int pageSize, String query) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CourseIndex> courseIndices = courseElasticsearchRepository.searchByPrefixOrMatch(query, pageable);
        return courseMapper.toDtoListForView(courseIndices, minioService, this);
    }
    @Override
    public Course getById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new EntityNotFoundException("Course with id = "+courseId+" not found")
        );
    }
    @Override
    public CourseResponseForAdd getByIdForAdd(Long id) {
        return courseMapper.toDtoForAdd(getById(id), minioService);
    }
    @Override
    @SneakyThrows
    public Long add(CourseRequestForAdd courseRequestForAdd) {
        Course course = courseMapper.toEntityForAdd(courseRequestForAdd, this, professorService, studentService);
        if(courseRequestForAdd.getImage() != null)course.setImage(minioService.putMultipartFile(courseRequestForAdd.getImage()));
        course.setStatusCourse(StatusCourse.ACTIVE);
        return save(course).getId();
    }
    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }
    @Override
    public void removeById(long id) {
        Course course = getById(id);
        course.setStatusCourse(StatusCourse.DISABLE);
        save(course);
    }
}