package ua.kpi.edutrackeradmin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.repository.LessonRepository;
import ua.kpi.edutrackeradmin.service.LessonService;

@Log4j2
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    @Override
    public long countByCourseId(long courseId) {
        log.info("LessonServiceImpl countByCourseId start");
        long count = lessonRepository.countByCourseId(courseId);
        log.info("LessonServiceImpl countByCourseId finish");
        return count;
    }
}
