package ua.kpi.edutrackeradmin.service;

import org.springframework.data.domain.Page;
import ua.kpi.edutrackeradmin.dto.course.CourseRequestForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseForAdd;
import ua.kpi.edutrackeradmin.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackerentity.entity.Course;

public interface CourseService {
    Page<CourseResponseViewAll> getAll(int page, int pageSize, String query);
    Course getById(Long id);
    CourseResponseForAdd getByIdForAdd(Long id);
    Long add(CourseRequestForAdd courseRequestForAdd);
    Course save(Course course);
    void removeById(long id);
}