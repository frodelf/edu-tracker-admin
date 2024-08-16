package ua.kpi.edutrackeradmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.edutrackerentity.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
