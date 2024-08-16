package ua.kpi.edutrackeradmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.edutrackerentity.entity.Manager;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByEmail(String email);
}