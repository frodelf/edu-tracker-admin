package ua.kpi.edutrackeradmin.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForGlobal;
import ua.kpi.edutrackeradmin.dto.manager.ManagerRequestForAdd;
import ua.kpi.edutrackeradmin.dto.manager.ManagerResponseForViewAll;
import ua.kpi.edutrackeradmin.service.impl.ManagerServiceImpl;
import ua.kpi.edutrackerentity.entity.Manager;
import ua.kpi.edutrackerentity.entity.enums.Role;

import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static ua.kpi.edutrackeradmin.validation.ValidUtil.notNullAndBlank;

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

    public Page<ManagerResponseForViewAll> toDtoList(Page<Manager> managers) {
        return new PageImpl<>(managers.getContent().stream()
                .map(this::toDtoForViewAll)
                .collect(Collectors.toList()), managers.getPageable(), managers.getTotalElements());
    }
    private ManagerResponseForViewAll toDtoForViewAll(Manager manager) {
        ManagerResponseForViewAll managerResponseForViewAll = new ManagerResponseForViewAll();
        managerResponseForViewAll.setId(manager.getId());
        managerResponseForViewAll.setFullName(
                (manager.getLastName() != null ? manager.getLastName() : "") + " " +
                        (manager.getName() != null ? manager.getName() : "") + " " +
                        (manager.getMiddleName() != null ? manager.getMiddleName() : "")
        );
        managerResponseForViewAll.setTelegram(manager.getTelegram());
        managerResponseForViewAll.setPhone(manager.getPhone());
        managerResponseForViewAll.setEmail(manager.getEmail());
        return managerResponseForViewAll;
    }
    public Manager toEntityForAdd(ManagerRequestForAdd managerRequestForAdd, ManagerServiceImpl managerService) {
        Manager manager = new Manager();
        if(nonNull(managerRequestForAdd.getId()))manager = managerService.getById(managerRequestForAdd.getId());
        if(notNullAndBlank(managerRequestForAdd.getLastName()))manager.setLastName(managerRequestForAdd.getLastName());
        if(notNullAndBlank(managerRequestForAdd.getName()))manager.setName(managerRequestForAdd.getName());
        if(notNullAndBlank(managerRequestForAdd.getMiddleName()))manager.setMiddleName(managerRequestForAdd.getMiddleName());
        if(notNullAndBlank(managerRequestForAdd.getEmail()))manager.setEmail(managerRequestForAdd.getEmail());
        if(notNullAndBlank(managerRequestForAdd.getPhone()))manager.setPhone(managerRequestForAdd.getPhone());
        if(notNullAndBlank(managerRequestForAdd.getTelegram()))manager.setTelegram(managerRequestForAdd.getTelegram());
        //TODO переробити коли буде розсилка
        manager.setPassword(new BCryptPasswordEncoder().encode("password"));
        return manager;
    }
}