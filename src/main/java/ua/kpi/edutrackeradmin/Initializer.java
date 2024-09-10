package ua.kpi.edutrackeradmin;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ua.kpi.edutrackeradmin.repository.ManagerRepository;
import ua.kpi.edutrackerentity.entity.Manager;
import ua.kpi.edutrackerentity.entity.enums.Role;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {
    private final ManagerRepository managerRepository;
    @Override
    @Transactional
    public void run(String... args) {
        if(managerRepository.count() == 0) {
            Manager manager = new Manager();
            manager.setLastName("Main");
            manager.setName("Admin");
            manager.setEmail("admin@gmail.com");
            manager.setPassword(new BCryptPasswordEncoder().encode("admin"));
            manager.setRole(Role.ROLE_ADMIN);
            managerRepository.save(manager);
        }
    }
}