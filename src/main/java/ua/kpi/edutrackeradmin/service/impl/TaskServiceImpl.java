package ua.kpi.edutrackeradmin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.repository.TaskRepository;
import ua.kpi.edutrackeradmin.service.TaskService;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Override
    public long countAllTasksByCourseId(long courseId) {
        return taskRepository.countAllByCourseId(courseId);
    }
    @Override
    public long countAllOpenTasksByCourseId(long courseId) {
        return taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.OPEN);
    }
    @Override
    public long countAllCloseTasksByCourseId(long courseId) {
        return taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.CLOSE);
    }
}
