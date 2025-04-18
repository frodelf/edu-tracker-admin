package ua.kpi.edutrackeradmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.edutrackerentity.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByPhone(String phone);
    Optional<User> findFirstByTelegram(String telegram);
    Optional<User> findFirstByEmail(String email);


    boolean existsByEmail(String email);
    boolean existsByTelegram(String telegram);
    boolean existsByPhone(String phone);
}