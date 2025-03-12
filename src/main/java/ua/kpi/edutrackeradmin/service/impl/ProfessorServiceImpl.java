package ua.kpi.edutrackeradmin.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.dto.ForSelect2Dto;
import ua.kpi.edutrackeradmin.repository.ProfessorRepository;
import ua.kpi.edutrackeradmin.service.ProfessorService;
import ua.kpi.edutrackerentity.entity.Professor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;

    @Override
    public Professor getById(Long professorId) {
        log.info("ProfessorServiceImpl getById start");
        Professor professor = professorRepository.findById(professorId).orElseThrow(
                () -> new EntityNotFoundException("Professor with id = "+professorId+" not found")
        );
        log.info("ProfessorServiceImpl getById finish");
        return professor;
    }

    @Override
    public Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto) {
        log.info("ProfessorServiceImpl getAllByGroupForSelect start");

        Pageable pageable = PageRequest.of(forSelect2Dto.getPage(), forSelect2Dto.getSize(), Sort.by(Sort.Order.desc("id")));
        Specification<Professor> specification = (root, query, criteriaBuilder) -> {
            String searchQuery = "%" + forSelect2Dto.getQuery().toLowerCase() + "%";
            return criteriaBuilder.like(
                    criteriaBuilder.concat(
                            criteriaBuilder.lower(root.get("lastName")),
                            criteriaBuilder.concat(" ", criteriaBuilder.lower(root.get("name")))
                    ),
                    searchQuery
            );
        };
        Page<Professor> professors = professorRepository.findAll(specification, pageable);
        List<Map<String, String>> list = new ArrayList<>();
        for (Professor professor : professors.getContent()) {
            Map<String, String> map = new HashMap<>();
            map.put(professor.getId().toString(), professor.getLastName() + " " + professor.getName());
            list.add(map);
        }

        log.info("ProfessorServiceImpl getAllByGroupForSelect finish");
        return new PageImpl<>(list, pageable, professors.getTotalElements());
    }
}
