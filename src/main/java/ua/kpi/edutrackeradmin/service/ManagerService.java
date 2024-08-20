package ua.kpi.edutrackeradmin.service;

import org.springframework.data.domain.Page;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForGlobal;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForAdd;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForFilter;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForViewAll;
import ua.kpi.edutrackerentity.entity.Manager;

public interface ManagerService {
    Manager getByEmailForAuth(String email);
    Manager getByEmail(String email);
    boolean isAuthenticated();
    ManagerResponseForGlobal getAuthManagerForGlobal();
    Manager getAuthManager();
    Manager save(Manager manager);
    Page<ManagerResponseForViewAll> getAll(ManagerRequestForFilter managerRequestForFilter);
    Long add(ManagerRequestForAdd managerRequestForAdd);
    Manager getById(Long id);
}