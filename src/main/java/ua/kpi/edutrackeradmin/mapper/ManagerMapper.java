package ua.kpi.edutrackeradmin.mapper;

import ua.kpi.edutrackeradmin.dto.ManagerResponseForGlobal;
import ua.kpi.edutrackerentity.entity.Manager;
import ua.kpi.edutrackerentity.entity.enums.Role;

public class ManagerMapper {
    public ManagerResponseForGlobal toDtoForGlobal(Manager manager) {
        ManagerResponseForGlobal managerResponseForGlobal = new ManagerResponseForGlobal();
        managerResponseForGlobal.setId(manager.getId());
        managerResponseForGlobal.setLastName(manager.getLastName());
        managerResponseForGlobal.setName(manager.getName());
        managerResponseForGlobal.setEmail(manager.getEmail());
        managerResponseForGlobal.setImage(manager.getImage());
        managerResponseForGlobal.setIsMainAdmin(manager.getRole().equals(Role.ROLE_ADMIN));
        return managerResponseForGlobal;
    }
}