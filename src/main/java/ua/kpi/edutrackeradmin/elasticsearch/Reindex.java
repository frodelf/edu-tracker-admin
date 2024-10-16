package ua.kpi.edutrackeradmin.elasticsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class Reindex {
    private final CourseRepository courseRepository;
    private final CourseElasticsearchRepository courseElasticsearchRepository;
    public void reindex() {
        courseElasticsearchRepository.saveAll(
                courseRepository.findAll().stream().map(
                        course -> new CourseIndex(
                                course.getId(),
                                course.getName(),
                                course.getGoal()
                        )
                ).toList()
        );
    }
}