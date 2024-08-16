package ua.kpi.edutrackeradmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Task;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Long countAllByCourseId(Long courseId);
    Long countAllByCourseIn(List<Course> courses);
    Long countAllByCourseIdAndStatus(Long courseId, StatusTask status);
}