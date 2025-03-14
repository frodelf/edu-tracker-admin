package ua.kpi.edutrackeradmin.service;

import org.springframework.data.domain.Page;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackerentity.entity.Professor;

import java.util.Map;

public interface ProfessorService {
    Professor getById(Long professorId);
    Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto);
}