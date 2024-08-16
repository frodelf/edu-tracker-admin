package ua.kpi.edutrackeradmin.service;

public interface TaskService {
    long countAllTasksByCourseId(long courseId);
    long countAllOpenTasksByCourseId(long courseId);
    long countAllCloseTasksByCourseId(long courseId);
}