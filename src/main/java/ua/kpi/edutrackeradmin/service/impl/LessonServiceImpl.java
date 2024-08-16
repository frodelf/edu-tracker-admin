package ua.kpi.edutrackeradmin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.repository.LessonRepository;
import ua.kpi.edutrackeradmin.service.LessonService;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    @Override
    public long countByCourseId(long courseId) {
        return lessonRepository.countByCourseId(courseId);
    }
}