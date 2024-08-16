package ua.kpi.edutrackeradmin.service;

import ua.kpi.edutrackerentity.entity.Professor;

public interface ProfessorService {
    Professor getById(Long professorId);
}