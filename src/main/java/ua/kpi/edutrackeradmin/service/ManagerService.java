package ua.kpi.edutrackeradmin.service;

import ua.kpi.edutrackeradmin.dto.ManagerResponseForGlobal;
import ua.kpi.edutrackerentity.entity.Manager;

public interface ManagerService {
    Manager getByEmailForAuth(String email);
    Manager getByEmail(String email);
    boolean isAuthenticated();
    ManagerResponseForGlobal getAuthProfessorForGlobal();
    Manager getAuthManager();
    Manager save(Manager manager);
}