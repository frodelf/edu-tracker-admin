package ua.kpi.edutrackeradmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.kpi.edutrackeradmin.dto.ManagerResponseForGlobal;
import ua.kpi.edutrackeradmin.service.ManagerService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    private final ManagerService managerService;

    @ModelAttribute("manager")
    public ManagerResponseForGlobal globalTeamLeadAttribute() {
        return managerService.getAuthProfessorForGlobal();
    }
}