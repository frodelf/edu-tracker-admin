package ua.kpi.edutrackeradmin.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackeradmin.dto.ManagerResponseForGlobal;
import ua.kpi.edutrackeradmin.mapper.ManagerMapper;
import ua.kpi.edutrackeradmin.repository.ManagerRepository;
import ua.kpi.edutrackeradmin.service.ManagerService;
import ua.kpi.edutrackerentity.entity.Manager;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerMapper managerMapper = new ManagerMapper();
    private final ManagerRepository managerRepository;

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
    public ManagerResponseForGlobal getAuthManagerForGlobal() {
        try {
            return managerMapper.toDtoForGlobal(getAuthManager());
        }catch (InsufficientAuthenticationException e){
            return null;
        }
    }
}