package ua.kpi.edutrackeradmin.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.repository.ProfessorRepository;
import ua.kpi.edutrackeradmin.service.ProfessorService;
import ua.kpi.edutrackerentity.entity.Professor;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;
    @Override
    public Professor getById(Long professorId) {
        return professorRepository.findById(professorId).orElseThrow(
                () -> new EntityNotFoundException("Professor with id = "+professorId+" not found")
        );
    }
}