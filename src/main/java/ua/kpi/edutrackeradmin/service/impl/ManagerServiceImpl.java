package ua.kpi.edutrackeradmin.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerMapper managerMapper = new ManagerMapper();
    private final ManagerRepository managerRepository;
    private final EmailService emailService;

    @Override
    public Manager getByEmailForAuth(String email) {
        return managerRepository.findByEmail(email).orElseThrow(
                () -> new AuthenticationCredentialsNotFoundException("Credential isn't correct")
        );
    }
    @Override
    public Manager getByEmail(String email) {
        return managerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Manager with email = "+email+" not found")
        );
    }
    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return !(authentication instanceof AnonymousAuthenticationToken);
    }
    @Override
    public Manager getAuthManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            String currentUserName = authentication.getName();
            return getByEmail(currentUserName);
        }
        else throw new InsufficientAuthenticationException("The professor is not authorized");
    }
    @Override
    @Transactional
    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Page<ManagerResponseForViewAll> getAll(ManagerRequestForFilter managerRequestForFilter) {
        Pageable pageable = PageRequest.of(managerRequestForFilter.getPage(), managerRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Specification<Manager> specification = new ManagerSpecification(managerRequestForFilter);
        return managerMapper.toDtoList(managerRepository.findAll(specification, pageable));
    }

    @Override
    @Transactional
    public Long add(ManagerRequestForAdd managerRequestForAdd) {
        Manager manager = managerMapper.toEntityForAdd(managerRequestForAdd, this);
        manager.setPassword(new BCryptPasswordEncoder().encode(generateAndSendPassword(manager.getEmail())));
        return save(manager).getId();
    }
    @Override
    public Manager getById(Long id) {
        return managerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Manager with id = "+id+" not found")
        );
    }
    @Override
    public ManagerResponseForGlobal getAuthManagerForGlobal() {
        try {
            return managerMapper.toDtoForGlobal(getAuthManager());
        }catch (InsufficientAuthenticationException e){
            return null;
        }
    }
    private String generateAndSendPassword(String email) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            password.append(digit);
        }
        String passwordText = password.toString();
        emailService.sendEmail("password", passwordText, email);
        return passwordText;
    }
}