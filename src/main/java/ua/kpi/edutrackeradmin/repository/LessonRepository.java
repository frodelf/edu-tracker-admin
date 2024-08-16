package ua.kpi.edutrackeradmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.edutrackerentity.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    long countByCourseId(long courseId);
}