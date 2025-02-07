package ua.kpi.edutrackeradmin.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForGlobal;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForAdd;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForFilter;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForViewAll;
import ua.kpi.edutrackeradmin.mapper.ManagerMapper;
import ua.kpi.edutrackeradmin.repository.ManagerRepository;
import ua.kpi.edutrackeradmin.service.EmailService;
import ua.kpi.edutrackeradmin.service.ManagerService;
import ua.kpi.edutrackeradmin.specification.ManagerSpecification;
import ua.kpi.edutrackerentity.entity.Manager;
import ua.kpi.edutrackerentity.entity.enums.Role;

import java.security.SecureRandom;

@Log4j2
@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerMapper managerMapper = new ManagerMapper();
    private final ManagerRepository managerRepository;
    private final EmailService emailService;

    @Override
    public Manager getByEmailForAuth(String email) {
        log.info("ManagerServiceImpl getByEmailForAuth start");
        Manager manager = managerRepository.findByEmail(email).orElseThrow(
                () -> new AuthenticationCredentialsNotFoundException("Credential isn't correct")
        );
        log.info("ManagerServiceImpl getByEmailForAuth finish");
        return manager;
    }

    @Override
    public Manager getByEmail(String email) {
        log.info("ManagerServiceImpl getByEmail start");
        Manager manager = managerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Manager with email = "+email+" not found")
        );
        log.info("ManagerServiceImpl getByEmail finish");
        return manager;
    }

    @Override
    public boolean isAuthenticated() {
        log.info("ManagerServiceImpl isAuthenticated start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        log.info("ManagerServiceImpl isAuthenticated finish");
        return isAuthenticated;
    }

    @Override
    public Manager getAuthManager() {
        log.info("ManagerServiceImpl getAuthManager start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            String currentUserName = authentication.getName();
            Manager manager = getByEmail(currentUserName);
            log.info("ManagerServiceImpl getAuthManager finish");
            return manager;
        } else {
            log.error("ManagerServiceImpl getAuthManager error, unauthorized access");
            throw new InsufficientAuthenticationException("The professor is not authorized");
        }
    }

    @Override
    @Transactional
    public Manager save(Manager manager) {
        log.info("ManagerServiceImpl save start");
        Manager result = managerRepository.save(manager);
        log.info("ManagerServiceImpl save finish");
        return result;
    }

    @Override
    public Page<ManagerResponseForViewAll> getAll(ManagerRequestForFilter managerRequestForFilter) {
        log.info("ManagerServiceImpl getAll start");
        Pageable pageable = PageRequest.of(managerRequestForFilter.getPage(), managerRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Specification<Manager> specification = new ManagerSpecification(managerRequestForFilter);
        Page<ManagerResponseForViewAll> result = managerMapper.toDtoList(managerRepository.findAll(specification, pageable));
        log.info("ManagerServiceImpl getAll finish");
        return result;
    }

    @Override
    @Transactional
    public Long add(ManagerRequestForAdd managerRequestForAdd) {
        log.info("ManagerServiceImpl add start");
        Manager manager = managerMapper.toEntityForAdd(managerRequestForAdd, this);
        manager.setPassword(new BCryptPasswordEncoder().encode(generateAndSendPassword(manager.getEmail())));
        manager.setRole(Role.ROLE_MANAGER);
        Long result = save(manager).getId();
        log.info("ManagerServiceImpl add finish");
        return result;
    }

    @Override
    public Manager getById(Long id) {
        log.info("ManagerServiceImpl getById start, id: {}", id);
        Manager manager = managerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Manager with id = "+id+" not found")
        );
        log.info("ManagerServiceImpl getById finish, id: {}", id);
        return manager;
    }

    @Override
    public ManagerResponseForGlobal getAuthManagerForGlobal() {
        log.info("ManagerServiceImpl getAuthManagerForGlobal start");
        try {
            ManagerResponseForGlobal managerResponseForGlobal = managerMapper.toDtoForGlobal(getAuthManager());
            log.info("ManagerServiceImpl getAuthManagerForGlobal finish");
            return managerResponseForGlobal;
        } catch (InsufficientAuthenticationException e) {
            log.warn("ManagerServiceImpl getAuthManagerForGlobal error: {}", e.getMessage());
            return null;
        }
    }

    private String generateAndSendPassword(String email) {
        log.info("ManagerServiceImpl generateAndSendPassword start");
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            password.append(digit);
        }
        String passwordText = password.toString();
        emailService.sendEmail("password", passwordText, email);
        log.info("ManagerServiceImpl generateAndSendPassword finish");
        return passwordText;
    }
}
