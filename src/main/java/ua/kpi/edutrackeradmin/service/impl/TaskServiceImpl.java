package ua.kpi.edutrackeradmin.service.impl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.repository.TaskRepository;
import ua.kpi.edutrackeradmin.service.TaskService;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;

@Log4j2
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public long countAllTasksByCourseId(long courseId) {
        log.info("TaskServiceImpl countAllTasksByCourseId start");
        long result = taskRepository.countAllByCourseId(courseId);
        log.info("TaskServiceImpl countAllTasksByCourseId finish");
        return result;
    }

    @Override
    public long countAllOpenTasksByCourseId(long courseId) {
        log.info("TaskServiceImpl countAllOpenTasksByCourseId start");
        long result = taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.OPEN);
        log.info("TaskServiceImpl countAllOpenTasksByCourseId finish");
        return result;
    }

    @Override
    public long countAllCloseTasksByCourseId(long courseId) {
        log.info("TaskServiceImpl countAllCloseTasksByCourseId start");
        long result = taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.CLOSE);
        log.info("TaskServiceImpl countAllCloseTasksByCourseId finish");
        return result;
    }
}
